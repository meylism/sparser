package com.meylism.sparser.predicate;

import com.meylism.sparser.rf.RawFilter;
import com.meylism.sparser.rf.UTFSubstringSearchRF;

import java.util.ArrayList;

public class ExactMatchPredicate extends SimplePredicate {

  public ExactMatchPredicate(String key, PredicateValue value) {
    super(key, value);
  }

  @Override
  public void compileRawFilters() {
    ArrayList<RawFilter> filters = new ArrayList<>();
    // add non-tokenized version as well
//    filters.add(new SubstringSearchRF(this.key));
//    for (String token : Tokenizer.tokenize(this.key)) {
//      filters.add(new SubstringSearchRF(token));
//    }

    filters.add(new UTFSubstringSearchRF(this.value.getValue()));
    for (String token : Tokenizer.tokenize(this.value.getValue())) {
      filters.add(new UTFSubstringSearchRF(token));
    }

    this.rawFilters = filters;
  }
}
