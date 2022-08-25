package com.meylism.sparser.benchmark;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meylism.sparser.JacksonDeserializer;
import com.meylism.sparser.Sparser;
import com.meylism.sparser.deserializer.Deserializer;
import com.meylism.sparser.predicate.ConjunctiveClause;
import com.meylism.sparser.predicate.ExactMatchPredicate;
import com.meylism.sparser.predicate.PredicateValue;
import com.meylism.sparser.FileFormat;
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
  private Sparser sparser = new Sparser(FileFormat.JSON);
  private ObjectMapper mapper = new ObjectMapper();
  public ArrayList<ConjunctiveClause> clauses = new ArrayList<>();

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
    Deserializer deserializer = new JacksonDeserializer();

    // Construct the query
    // SELECT * FROM table WHERE text = "Elon" OR text = "Putin" OR text = "Biden"
    ConjunctiveClause clause1 = new ConjunctiveClause();
    ConjunctiveClause clause2 = new ConjunctiveClause();
    ConjunctiveClause clause3 = new ConjunctiveClause();


    ExactMatchPredicate esmp1 = new ExactMatchPredicate("text", new PredicateValue("Elon"));
    ExactMatchPredicate esmp2 = new ExactMatchPredicate("text", new PredicateValue("Putin"));
    ExactMatchPredicate esmp3 = new ExactMatchPredicate("text", new PredicateValue("Biden"));

    clause1.add(esmp1);
    clause2.add(esmp2);
    clause3.add(esmp3);

    clauses.add(clause1);
    clauses.add(clause2);
    clauses.add(clause3);

    sparser.compile(clauses);
    sparser.calibrate(lines, deserializer);
  }

  public static void main(String[] args) throws RunnerException {
    Options opt = new OptionsBuilder()
        .include(SparserBenchmark.class.getSimpleName())
        .build();
    new Runner(opt).run();
  }
}
