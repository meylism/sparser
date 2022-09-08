package com.meylism.sparser.rf;

import lombok.Getter;
import lombok.ToString;

@ToString public abstract class SubstringSearchRF extends RawFilter {
  @Getter private final String token;

  protected SubstringSearchRF(final String token) {
    super();
    this.token = token;
  }
}
