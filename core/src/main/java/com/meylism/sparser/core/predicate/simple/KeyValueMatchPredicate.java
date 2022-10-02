package com.meylism.sparser.core.predicate.simple;

import com.meylism.sparser.core.predicate.PredicateKey;
import com.meylism.sparser.core.predicate.SimplePredicate;
import com.meylism.sparser.core.support.PredicateSupport;

import java.util.List;

public class KeyValueMatchPredicate extends SimplePredicate {
  private List<Character> delimiters;

  public KeyValueMatchPredicate(PredicateKey key, Object value) {
    super(key, value);
  }

  @Override public PredicateSupport getType() {
    return PredicateSupport.KEY_VALUE_MATCH;
  }
}
