package com.meylism.sparser.predicate;

import com.meylism.sparser.support.PredicateSupport;

public class ExactMatchPredicate extends SimplePredicate {

  public ExactMatchPredicate(String key, PredicateValue value) {
    super(key, value);
  }

  @Override public PredicateSupport getType() {
    return PredicateSupport.EXACT_STRING_MATCH;
  }
}
