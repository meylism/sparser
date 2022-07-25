package com.meylism.sparser.predicate;

import com.meylism.sparser.rf.RawFilter;
import com.meylism.sparser.rf.SubstringSearchRF;

import java.util.ArrayList;

public class ExactStringMatchPredicate extends SimplePredicate {

  public ExactStringMatchPredicate(String key, PredicateValue value) {
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

    filters.add(new SubstringSearchRF(this.value.getValue()));
    for (String token : Tokenizer.tokenize(this.value.getValue())) {
      filters.add(new SubstringSearchRF(token));
    }

    this.rawFilters = filters;
  }
}
