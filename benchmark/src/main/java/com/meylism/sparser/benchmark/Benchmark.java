package com.meylism.sparser.benchmark;

import com.fasterxml.jackson.core.io.JsonEOFException;
import com.meylism.sparser.benchmark.state.BenchmarkState;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

@OutputTimeUnit(value = TimeUnit.SECONDS)
@Warmup(iterations = 1, time = 5)
@Measurement(iterations = 2, time = 5)
@BenchmarkMode(value = Mode.AverageTime)
@Fork(value = 1)
@State(Scope.Benchmark)
public class Benchmark {

  @org.openjdk.jmh.annotations.Benchmark
  public void baseline(BenchmarkState state, Blackhole blackhole) throws Exception {
    try(BufferedReader scanner = new BufferedReader(new FileReader(state.getFile()))) {
      String s = scanner.readLine();
      while (s != null) {
        blackhole.consume(state.getDeserializer().deserialize(s));
        s = scanner.readLine();
      }
    }
  }

  @org.openjdk.jmh.annotations.Benchmark
  public void rawRead(BenchmarkState state, Blackhole blackhole) throws Exception {
    try(BufferedReader scanner = new BufferedReader(new FileReader(state.getFile()))) {
      String s = scanner.readLine();
      while (s != null) {
        blackhole.consume(s);
        s = scanner.readLine();
      }
    }
  }

  @org.openjdk.jmh.annotations.Benchmark
  public void sparser(BenchmarkState state, Blackhole blackhole) throws Exception {
    int filteredRecords = 0;
    int recordsSoFar = 0;

    try(BufferedReader scanner = new BufferedReader(new FileReader(state.getFile()))) {
      String s = scanner.readLine();
      while (s != null) {
        if (state.getSparser().filter(s)) {
          filteredRecords++;
        } else {
          blackhole.consume(state.getDeserializer().deserialize(s));
        }
        recordsSoFar++;
        s = scanner.readLine();
      }
    }

    System.out.println("\n ------------ STATISTICS ------------ \n");
    System.out.println("Total: " + recordsSoFar);
    System.out.println("Filtered: " + filteredRecords);

    float selectivity;
    if (recordsSoFar == 0) {
      System.out.println("Stopping benchmarks: dataset contains no records");
      System.exit(-1);
    } else {
      selectivity = (float) (recordsSoFar-filteredRecords)/(float)recordsSoFar;
      System.out.println("Selectivity: " +  selectivity);
    }
  }

  public void bench(String[] queries, String dataset) throws RunnerException {
      Options options = new OptionsBuilder()
          .include(getClass().getSimpleName())
          // the query to be benchmarked is passed here
          .param("query", queries)
          .param("dataset", dataset)
          .build();

      new Runner(options).run();
  }
}
