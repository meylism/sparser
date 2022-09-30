package com.meylism.sparser.predicate.simple;

import com.meylism.sparser.Configuration;
import com.meylism.sparser.operator.compiler.RawFilterCompiler;
import com.meylism.sparser.predicate.PredicateKey;
import com.meylism.sparser.predicate.SimplePredicate;
import com.meylism.sparser.support.PredicateSupport;

public class ExactMatchPredicate extends SimplePredicate {

  public ExactMatchPredicate(PredicateKey key, Object value) {
    super(key, value);
  }

  @Override public PredicateSupport getType() {
    return PredicateSupport.EXACT_MATCH;
  }
}
