package com.meylism.sparser.filter;

import com.meylism.sparser.Configuration;
import com.meylism.sparser.rf.RawFilter;

import java.util.List;

public class Filter {
  private Configuration configuration;

  public Filter(Configuration configuration) {
    this.configuration = configuration;
  }

  public Boolean filter(Object record) {
    //    int passed = 0;
    for (RawFilter rawFilter : this.configuration.getBestCascade()) {
      if (rawFilter.evaluate(record))
        return true;
    }
    return false;
  }
}
