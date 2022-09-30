package com.meylism.sparser.predicate;

import com.meylism.sparser.support.PredicateSupport;

import java.util.List;

public abstract class Predicate {
  public abstract List<Predicate> getChildren();
  public abstract PredicateSupport getType();
}
