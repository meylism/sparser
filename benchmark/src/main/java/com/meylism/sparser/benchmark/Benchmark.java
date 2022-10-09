package com.meylism.sparser.benchmark;

import com.meylism.sparser.benchmark.state.BenchmarkState;
import com.meylism.sparser.benchmark.state.BenchmarkStatsState;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@OutputTimeUnit(value = TimeUnit.SECONDS)
@Warmup(iterations = 2, time = 3)
@BenchmarkMode(value = Mode.AverageTime)
@Fork(value = 1)
@Measurement(iterations = 2, time = 3)
public class Benchmark {

  @org.openjdk.jmh.annotations.Benchmark
  public void baseline(BenchmarkState state, Blackhole blackhole) throws Exception {
    for (String record : state.getReader().read()) {
      blackhole.consume(state.getDeserializer().deserialize(record));
    }
  }

  @org.openjdk.jmh.annotations.Benchmark
  public void sparser(BenchmarkState state, BenchmarkStatsState statsState, Blackhole blackhole) throws Exception {
    for (String record : state.getReader().read()) {
      if (state.getSparser().filter(record)) {
        statsState.filteredRecords++;
      } else {
        blackhole.consume(state.getDeserializer().deserialize(record));
      }
      statsState.recordsSoFar++;
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
