package com.meylism.sparser.predicate;

import com.meylism.sparser.rf.RawFilter;
import lombok.Getter;

import java.util.ArrayList;

public abstract class SimplePredicate {
  final String key;
  final PredicateValue value;
  @Getter
  ArrayList<RawFilter> rawFilters;

  SimplePredicate(final String key, final PredicateValue value) {
    this.key = key;
    this.value = value;
  }

  public abstract void compileRawFilters();
}
