package com.meylism.sparser.rf;

import lombok.Getter;
import lombok.Setter;

import java.util.BitSet;

public abstract class RawFilter {
  @Getter @Setter
  private long avgRuntime;
  @Getter
  private BitSet passthroughMask;

  public abstract Boolean evaluate(final Object record);

  public void initPassthroughMask(int size) {
    this.passthroughMask = new BitSet(size);
  }

  public void setPassthroughMaskBit(int bitIndex) {
    this.passthroughMask.set(bitIndex);
  }
}
