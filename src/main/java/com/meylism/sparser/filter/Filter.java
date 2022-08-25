package com.meylism.sparser.filter;

import com.meylism.sparser.Configuration;

public abstract class Filter {
  protected Configuration configuration;

  protected Filter(Configuration configuration) {
    this.configuration = configuration;
  }
  public abstract Boolean filter(Object record);
}
