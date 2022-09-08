package com.meylism.sparser.predicate;

import com.meylism.sparser.rf.RawFilter;
import com.meylism.sparser.support.PredicateSupport;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public abstract class SimplePredicate {
  @Getter protected final PredicateKey key;
  @Getter protected final PredicateValue value;
  @Setter @Getter protected Boolean inverted;
  @Setter @Getter protected ArrayList<RawFilter> rawFilters;

  protected SimplePredicate(final PredicateKey key, final PredicateValue value) {
    this.key = key;
    this.value = value;
    this.inverted = false;
  }

  public abstract PredicateSupport getType();
}
