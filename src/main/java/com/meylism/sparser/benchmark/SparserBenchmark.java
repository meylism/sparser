package com.meylism.sparser.benchmark;

import com.meylism.sparser.FileFormat;
import com.meylism.sparser.Sparser;
import com.meylism.sparser.deserializer.Deserializer;
import com.meylism.sparser.predicate.PredicateKey;
import com.meylism.sparser.predicate.SimplePredicate;
import com.meylism.sparser.predicate.junction.And;
import com.meylism.sparser.predicate.junction.Or;
import com.meylism.sparser.predicate.simple.ExactMatchPredicate;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@Warmup(iterations = 3, time = 5)
@Measurement(iterations = 3, time = 5)
@Fork(1)
@BenchmarkMode({Mode.AverageTime})
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class SparserBenchmark {
  private ArrayList<String> lines;
  private Sparser sparser;
  private Deserializer deserializer;

  @Benchmark
  public void benchSparser(Blackhole bh) throws Exception {
    for (String record : lines) {
      if (!sparser.filter(record)){
        bh.consume(deserializer.deserialize(record));
//        System.out.println(record);
      }
    }
  }

  @Benchmark
  public void benchWithoutSparser(Blackhole bh) throws Exception {
    for(String line : lines) {
      bh.consume(deserializer.deserialize(line));
    }
  }

  @Setup
  public void setup() throws Exception {
    lines = Utils.loadJson("benchmark/gharchive.json");
    deserializer = new JacksonDeserializer();

    And<SimplePredicate> clause1 = new And<>();
    And<SimplePredicate> clause2 = new And<>();
    And<SimplePredicate> clause3 = new And<>();

    ExactMatchPredicate esmp1 = new ExactMatchPredicate(new PredicateKey("text"), "musk");
    ExactMatchPredicate esmp2 = new ExactMatchPredicate(new PredicateKey("text"), "elon");
    ExactMatchPredicate esmp3 = new ExactMatchPredicate(new PredicateKey("text"), "biden");

    clause1.add(esmp1);
    clause2.add(esmp2);
    clause3.add(esmp3);

    Or<And<SimplePredicate>> predicate = new Or<>();
    predicate.add(clause1);
    predicate.add(clause2);
    predicate.add(clause3);

    sparser = new Sparser(predicate, FileFormat.JSON, new JacksonDeserializer());
  }

  public static void main(String[] args) throws RunnerException {
    Options opt = new OptionsBuilder()
        .include(SparserBenchmark.class.getSimpleName())
        .build();
    new Runner(opt).run();
  }
}
