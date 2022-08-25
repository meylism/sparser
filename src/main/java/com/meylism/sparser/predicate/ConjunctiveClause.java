package com.meylism.sparser.predicate;

import lombok.Getter;

import java.util.ArrayList;

public class ConjunctiveClause {
  @Getter private ArrayList<SimplePredicate> simplePredicates = new ArrayList<>();

  public void add(SimplePredicate simplePredicate) {
    this.simplePredicates.add(simplePredicate);
  }

  /**
   * Get the total number of raw filters in this clause.
   */
  public int getTotalNumberOfRFs() {
    int sum = 0;
    for (SimplePredicate predicate : simplePredicates)
      sum += predicate.getRawFilters().size();
    return sum;
  }
}
