package com.meylism.sparser.benchmark;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meylism.sparser.Sparser;
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
  private Sparser sparser;
  private ObjectMapper mapper;

  @Benchmark
  public void benchFakeSparser(Blackhole bh) throws Exception {
    sparser.calibrate(lines.subList(0, 20));

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
  public void setup() throws IOException {
    lines = Utils.loadJson("twitter.json");

    predicates = new ArrayList<>();
    predicates.add("text");
    predicates.add("source");

    mapper = new ObjectMapper();
    sparser = new Sparser(predicates);
  }

  public static void main(String[] args) throws RunnerException {
    Options opt = new OptionsBuilder()
        .include(FakeSparserBenchmark.class.getSimpleName())
        .build();
    new Runner(opt).run();
  }
}
