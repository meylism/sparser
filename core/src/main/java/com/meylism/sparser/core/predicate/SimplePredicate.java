package com.meylism.sparser.core.predicate;

import com.meylism.sparser.core.operator.FilterOperator;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

public abstract class SimplePredicate extends Predicate {
  @Getter protected final PredicateKey key;
  @Getter protected final String value;
  @Setter @Getter protected Boolean inverted;
  @Setter @Getter protected Set<FilterOperator> rawFilters;

  protected SimplePredicate(final PredicateKey key, final Object value) {
    this.key = key;
    this.value = initValue(value);
    this.inverted = false;
  }

  // SimplePredicate is a leaf node and therefore contains no child.
  public List<Predicate> getChildren() {
    return null;
  }

  protected String initValue(Object value) {
    return (String) value;
  }
}
