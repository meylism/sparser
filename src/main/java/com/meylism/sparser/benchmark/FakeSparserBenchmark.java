package com.meylism.sparser.benchmark;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@Warmup(iterations = 3, time = 3)
@Measurement(iterations = 3, time = 3)
@Fork(1)
@BenchmarkMode({Mode.AverageTime})
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class FakeSparserBenchmark {
  private ArrayList<String> lines;
  private ArrayList<String> predicates;
  private ObjectMapper mapper;

  @Benchmark
  public void benchFakeSparser(Blackhole bh) throws JsonProcessingException {
    int match = 0;
    for(String line : lines) {
      for(String predicate : predicates) {
        if (line.indexOf(predicate) > -1 )
          match++;
      }

      if(match == predicates.size()){
        bh.consume(mapper.readTree(line));
        System.out.println(line);
      }


      match = 0;
    }


  }

  @Benchmark
  public void benchWithoutSparser(Blackhole bh) throws JsonProcessingException {
    for(String line : lines) {
      bh.consume(mapper.readTree(line));
    }
  }

  @Setup
  public void setup() throws IOException {
    lines = Utils.loadJson("twitter.json");

    predicates = new ArrayList<>();
    predicates.add("meyl");
    predicates.add("ylis");

    mapper = new ObjectMapper();
  }

  public static void main(String[] args) throws RunnerException {
    Options opt = new OptionsBuilder()
        .include(FakeSparserBenchmark.class.getSimpleName())
        .build();
    new Runner(opt).run();
  }
}
