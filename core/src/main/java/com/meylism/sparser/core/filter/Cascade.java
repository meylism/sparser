package com.meylism.sparser.core.filter;

import com.meylism.sparser.core.operator.FilterOperator;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * A cascade is a list of a list of filter operators. Each list holds all the filter operators of a conjunction in
 * the give query.
 */
public class Cascade {
  @Getter private final List<Set<FilterOperator>> cascade;

  public Cascade() {
    this.cascade = new ArrayList<>();
  }

  public void addConjunction(Set<FilterOperator> conjunction) {
    this.cascade.add(conjunction);
  }
}
