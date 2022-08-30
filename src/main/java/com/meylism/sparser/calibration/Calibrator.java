package com.meylism.sparser.calibration;

import com.meylism.sparser.Configuration;
import com.meylism.sparser.rf.RawFilter;

import java.util.List;

public abstract class Calibrator {
  protected Configuration configuration;

  protected Calibrator(Configuration configuration) {
    this.configuration = configuration;
  }
  public abstract List<RawFilter> calibrate(List samples) throws Exception;
}
