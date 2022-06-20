package com.meylism.sparser.calibration;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Timing {
  private long samplingTotal;
  private long searchTotal;
  private long greppingTotal;

  private long parseCost;

  private long processed;
  private long skipped;

  private long total;
}
