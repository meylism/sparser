package com.meylism.sparser.benchmark;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meylism.sparser.Sparser;
import com.meylism.sparser.predicate.ConjunctiveClause;
import com.meylism.sparser.predicate.ExactStringMatchPredicate;
import com.meylism.sparser.predicate.PredicateValue;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Warmup(iterations = 3, time = 3)
@Measurement(iterations = 3, time = 3)
@Fork(1)
@BenchmarkMode({Mode.AverageTime})
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class SparserBenchmark {
  private ArrayList<String> lines;
  private ArrayList<String> predicates;
  private Sparser sparser = new Sparser();
  private ObjectMapper mapper = new ObjectMapper();
  public ArrayList<ConjunctiveClause> clauses = new ArrayList<>();

  @Benchmark
  public void benchSparser(Blackhole bh) throws Exception {
    sparser.compile(clauses);
    sparser.calibrate(lines);
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
    lines = Utils.loadJson("twitter2.json");

    ConjunctiveClause clause1 = new ConjunctiveClause();
    ConjunctiveClause clause3 = new ConjunctiveClause();

    ExactStringMatchPredicate esmp1 = new ExactStringMatchPredicate("text", new PredicateValue("Elon"));
    ExactStringMatchPredicate esmp3 = new ExactStringMatchPredicate("lang", new PredicateValue("en"));

    clause1.add(esmp1);
    clause3.add(esmp3);

    clauses.add(clause1);
//    clauses.add(clause3);
  }

  public static void main(String[] args) throws RunnerException {
    Options opt = new OptionsBuilder()
        .include(SparserBenchmark.class.getSimpleName())
        .build();
    new Runner(opt).run();
  }
}
