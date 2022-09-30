package com.meylism.sparser.predicate.junction;

import com.meylism.sparser.predicate.Predicate;
import com.meylism.sparser.support.PredicateSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Disjunction
 */
public class Or <T extends Predicate> extends Predicate {
  private List<T> predicates;

  public Or() {
    predicates = new ArrayList<>();
  }

  public void add(T predicate) {
    predicates.add(predicate);
  }

  @Override public PredicateSupport getType() {
    return PredicateSupport.OR;
  }

  @Override public List<Predicate> getChildren() {
    return (List<Predicate>) predicates;
  }
}
