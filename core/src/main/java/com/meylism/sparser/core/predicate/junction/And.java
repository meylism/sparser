package com.meylism.sparser.core.predicate.junction;

import com.meylism.sparser.core.predicate.Predicate;
import com.meylism.sparser.core.support.PredicateSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Conjunction
 */
public class And<T extends Predicate> extends Predicate {
  private List<T> predicates;

  public And() {
    predicates = new ArrayList<>();
  }

  public void add(T predicate) {
    predicates.add(predicate);
  }

  @Override public PredicateSupport getType() {
    return PredicateSupport.AND;
  }

  @Override public List<Predicate> getChildren() {
    return (List<Predicate>) predicates;
  }
}
