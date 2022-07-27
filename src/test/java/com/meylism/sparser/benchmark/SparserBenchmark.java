package com.meylism.sparser.benchmark;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meylism.sparser.Sparser;
import com.meylism.sparser.Sparser.SparserBuilder;
import com.meylism.sparser.Utils;
import com.meylism.sparser.predicate.ConjunctiveClause;
import com.meylism.sparser.predicate.ExactMatchPredicate;
import com.meylism.sparser.predicate.PredicateValue;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@Warmup(iterations = 3, time = 3)
@Measurement(iterations = 3, time = 3)
@Fork(1)
@BenchmarkMode({Mode.AverageTime})
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class SparserBenchmark {
  private ArrayList<String> lines;
  private Sparser sparser = new SparserBuilder().build();
  private ObjectMapper mapper = new ObjectMapper();
  public ArrayList<ConjunctiveClause> clauses = new ArrayList<>();

  @Benchmark
  public void benchSparserCalibration(Blackhole bh) throws Exception {
    sparser.compile(clauses);
    sparser.calibrate(lines);
    bh.consume(sparser);
  }

  @Benchmark
  public void benchSparser(Blackhole bh) throws Exception {
    for (String record : lines) {
      if (sparser.filter(record)){
        bh.consume(mapper.readTree(record));
//        System.out.println(record);
      }
    }
  }

  @Benchmark
  public void benchWithoutSparser(Blackhole bh) throws JsonProcessingException {
    for(String line : lines) {
      bh.consume(mapper.readTree(line));
    }
  }

  @Setup
  public void setup() throws Exception {
    lines = Utils.loadJson("benchmark/twitter2.json");
    System.out.println(lines.size());

    ConjunctiveClause clause1 = new ConjunctiveClause();
    ConjunctiveClause clause3 = new ConjunctiveClause();

    ExactMatchPredicate esmp1 = new ExactMatchPredicate("text", new PredicateValue("Elon"));
    ExactMatchPredicate esmp3 = new ExactMatchPredicate("text", new PredicateValue("Putin"));

    clause1.add(esmp1);
    clause3.add(esmp3);

    clauses.add(clause1);
    clauses.add(clause3);

    sparser.compile(clauses);
    sparser.calibrate(lines);
  }

  public static void main(String[] args) throws RunnerException {
    Options opt = new OptionsBuilder()
        .include(SparserBenchmark.class.getSimpleName())
        .build();
    new Runner(opt).run();
  }
}
