package com.meylism.sparser;

import lombok.Getter;

public class FilterSupport {
  @Getter
  private Predicate predicate;
  @Getter
  private RawFilter rawFilter;

  FilterSupport(Predicate predicate, RawFilter rawFilter) {
    this.predicate = predicate;
    this.rawFilter = rawFilter;
  }
}
