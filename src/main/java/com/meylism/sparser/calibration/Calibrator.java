package com.meylism.sparser.calibration;

import com.meylism.sparser.Configuration;
import com.meylism.sparser.filter.Filter;
import com.meylism.sparser.rf.RawFilter;

import java.util.List;

public abstract class Calibrator {
  protected Configuration configuration;
  protected Filter filter;

  protected Calibrator(Configuration configuration, Filter filter) {
    this.configuration = configuration;
    this.filter = filter;
  }
  public abstract void calibrate(List samples) throws Exception;
  public abstract Filter getFilter();
  public abstract List<RawFilter> getBestCascade();
}
