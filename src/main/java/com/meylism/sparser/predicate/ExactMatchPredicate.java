package com.meylism.sparser.predicate;

import com.meylism.sparser.rf.RawFilter;
import com.meylism.sparser.rf.UTFSubstringSearchRF;

import java.util.ArrayList;

public class ExactMatchPredicate extends SimplePredicate {

  public ExactMatchPredicate(String key, PredicateValue value) {
    super(key, value);
  }
}
