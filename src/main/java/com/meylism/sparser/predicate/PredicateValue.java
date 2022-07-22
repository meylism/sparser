package com.meylism.sparser.predicate;

import lombok.Getter;

@Getter
public class PredicateValue {
  private final String value;

  public PredicateValue(String value) {
    this.value = value;
  }

  public PredicateValue(Integer value) {
    this.value = value.toString();
  }
}
