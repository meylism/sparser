package com.meylism.sparser.filter;

import com.meylism.sparser.Configuration;
import com.meylism.sparser.rf.RawFilter;

public class MinimalistFilter extends Filter {
  public MinimalistFilter(Configuration configuration) {
    super(configuration);
  }

  public Boolean filter(Object record) {
    //    int passed = 0;
    for (RawFilter rawFilter : this.configuration.getCalibrator().getBestCascade()) {
      if (rawFilter.evaluate(record))
        return true;
    }
    return false;
  }
}
