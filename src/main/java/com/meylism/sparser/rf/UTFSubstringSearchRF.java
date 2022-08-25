package com.meylism.sparser.rf;

public class UTFSubstringSearchRF extends SubstringSearchRF {
  public UTFSubstringSearchRF(String token) {
    super(token);
  }

  @Override public Boolean evaluate(final Object record) {
    return ((String) record).indexOf(getToken()) > -1;
  }
}
