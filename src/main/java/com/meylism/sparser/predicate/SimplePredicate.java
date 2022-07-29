package com.meylism.sparser.predicate;

import com.meylism.sparser.rf.RawFilter;
import com.meylism.sparser.RawFilterCompiler;
import lombok.Getter;

import java.util.ArrayList;

public abstract class SimplePredicate {
  @Getter
  final String key;
  @Getter
  final PredicateValue value;
  @Getter
  ArrayList<RawFilter> rawFilters;

  SimplePredicate(final String key, final PredicateValue value) {
    this.key = key;
    this.value = value;
  }

  public void compileRawFilters(RawFilterCompiler compiler) {
    this.rawFilters = compiler.compile(this);
  }
}