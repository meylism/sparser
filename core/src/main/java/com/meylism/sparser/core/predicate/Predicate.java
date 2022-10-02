package com.meylism.sparser.core.predicate;

import com.meylism.sparser.core.support.PredicateSupport;

import java.util.List;

public abstract class Predicate {
  public abstract List<Predicate> getChildren();

  public abstract PredicateSupport getType();
}
