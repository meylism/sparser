package com.meylism.sparser.predicate.junction;

import com.meylism.sparser.predicate.Predicate;
import com.meylism.sparser.support.PredicateSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Conjunction
 */
public class And <T extends Predicate> extends Predicate {
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
