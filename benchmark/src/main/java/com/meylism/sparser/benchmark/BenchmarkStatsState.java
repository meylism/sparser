package com.meylism.sparser.benchmark;

import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

@State(Scope.Benchmark)
public class BenchmarkStatsState {
  int recordsSoFar;
  int filteredRecords;

  @TearDown
  public void publishStats() {
    System.out.println("Selectivity:");
    System.out.println((float) filteredRecords/(float) recordsSoFar);
  }
}
