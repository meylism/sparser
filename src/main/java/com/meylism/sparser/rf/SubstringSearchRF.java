package com.meylism.sparser.rf;

import lombok.ToString;

import java.util.BitSet;

@ToString
public class SubstringSearchRF extends RawFilter {
  private final String token;

  public SubstringSearchRF(final String token) {
    this.token = token;
  }

  @Override
  public Boolean evaluate(final String record) {
    return record.indexOf(token) > -1;
  }
}
