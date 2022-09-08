package com.meylism.sparser.rf;

public class UTFSubstringSearchRF extends SubstringSearchRF {
  public UTFSubstringSearchRF(String token) {
    super(token);
  }

  @Override public Boolean evaluate(final Object record) {
    boolean result = ((String) record).indexOf(getToken()) > -1;
    return getInverted() ? !result : result;
  }
}
