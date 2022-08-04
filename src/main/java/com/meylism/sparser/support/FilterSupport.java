package com.meylism.sparser.support;

import lombok.Getter;

public class FilterSupport {
  @Getter
  private PredicateSupport predicateSupport;
  @Getter
  private RawFilterSupport rawFilterSupport;

  public FilterSupport(PredicateSupport predicateSupport, RawFilterSupport rawFilterSupport) {
    this.predicateSupport = predicateSupport;
    this.rawFilterSupport = rawFilterSupport;
  }
}
