package com.meylism.sparser.core.predicate.simple;

import com.meylism.sparser.core.predicate.PredicateKey;
import com.meylism.sparser.core.predicate.SimplePredicate;
import com.meylism.sparser.core.support.PredicateSupport;

public class ExactMatchPredicate extends SimplePredicate {

  public ExactMatchPredicate(PredicateKey key, Object value) {
    super(key, value);
  }

  @Override public PredicateSupport getType() {
    return PredicateSupport.EXACT_MATCH;
  }
}
