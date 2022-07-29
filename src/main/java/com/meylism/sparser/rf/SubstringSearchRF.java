package com.meylism.sparser.rf;

import lombok.Getter;

public abstract class SubstringSearchRF extends RawFilter {
  @Getter
  private final String token;

  public SubstringSearchRF(final String token) {
    this.token = token;
  }
}
