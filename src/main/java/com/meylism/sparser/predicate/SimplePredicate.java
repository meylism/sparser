package com.meylism.sparser.predicate;

import com.meylism.sparser.rf.RawFilter;
import com.meylism.sparser.support.PredicateSupport;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public abstract class SimplePredicate {
  @Getter protected final String key;
  @Getter protected final PredicateValue value;
  @Setter @Getter protected ArrayList<RawFilter> rawFilters;

  protected SimplePredicate(final String key, final PredicateValue value) {
    this.key = key;
    this.value = value;
  }

  public abstract PredicateSupport getType();
}
