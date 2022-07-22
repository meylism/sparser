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
public class BenchStringOps {
  @Param({"þ", "aþ", "a þ", "isþ", "is þ", "elonþ", "elon þ"})
  private String search;
  private String text;
  private ObjectMapper mapper;

  @Benchmark
  public void benchIndexOf(Blackhole bh) {
    int a = text.indexOf(search);
    bh.consume(a);
  }

  @Setup
  public void setup() throws IOException {
    text = Utils.loadJson("twitter2.json").toString();
  }

  public static void main(String[] args) throws RunnerException {
    Options opt = new OptionsBuilder()
        .include(BenchStringOps.class.getSimpleName())
        .build();
    new Runner(opt).run();
  }
}
