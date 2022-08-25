package com.meylism.sparser.predicate;

import lombok.Getter;

public class PredicateValue {
  @Getter private final String value;

  public PredicateValue(String value) {
    this.value = value;
  }

  public PredicateValue(Integer value) {
    this.value = value.toString();
  }
}
