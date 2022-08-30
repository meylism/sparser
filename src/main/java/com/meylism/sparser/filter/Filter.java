package com.meylism.sparser.filter;

import com.meylism.sparser.Configuration;
import com.meylism.sparser.rf.RawFilter;

import java.util.List;

public class Filter {
  private Configuration configuration;
  private List<RawFilter> bestCascade;

  public Filter(Configuration configuration, List<RawFilter> bestCascade) {
    this.configuration = configuration;
    this.bestCascade = bestCascade;
  }

  public Boolean filter(Object record) {
    //    int passed = 0;
    for (RawFilter rawFilter : bestCascade) {
      if (rawFilter.evaluate(record))
        return true;
    }
    return false;
  }
}
