package com.meylism.sparser.benchmark.state;

import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

/**
 * A state object used to do some analysis on benchmarks
 */
@State(Scope.Benchmark)
public class BenchmarkStatsState {
  public int recordsSoFar;
  public int filteredRecords;

  @TearDown
  public void publishStats() {
    System.out.println("\n ------------ STATISTICS ------------ \n");
    System.out.println("Total: " + recordsSoFar);
    System.out.println("Filtered: " + filteredRecords);
  }
}
