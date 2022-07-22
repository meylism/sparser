package com.meylism.sparser.rf;

import lombok.Getter;
import lombok.Setter;

import java.util.BitSet;

public abstract class RawFilter {
  @Getter @Setter
  private long avgRuntimeCost;
  @Getter @Setter
  private BitSet passthroughMask;

  public abstract Boolean evaluate(final String record);

  public void initPassthroughMask(int size) {
    this.passthroughMask = new BitSet(size);
  }

  public void maskSetBit(int bitIndex) {
    passthroughMask.set(bitIndex);
  }

  public void addToExecutionTime(long time) {
    this.avgRuntimeCost += time;
  }

  public void calculateAvgExecutionTime(int numberOfSamplesProcessed) {
    this.avgRuntimeCost = this.avgRuntimeCost / numberOfSamplesProcessed;
  }
}
