package com.meylism.sparser.core.operator.utf;

import com.meylism.sparser.core.operator.ExactMatchFilterOperator;

public class UTF8ExactMatchFilter extends ExactMatchFilterOperator {
  public UTF8ExactMatchFilter(String token) {
    super(token);
  }

  @Override public Boolean evaluate(final Object record) {
    boolean result = ((String) record).indexOf(getValue()) > -1;
    return getInverted() ? !result : result;
  }
}
