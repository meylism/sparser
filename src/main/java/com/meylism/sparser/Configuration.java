package com.meylism.sparser;

import com.meylism.sparser.support.FileFormat;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Configuration {
  @Setter
  private FileFormat fileFormat;

  // raw filter compilation
  /**
   * The size of to-be generated tokens for RFs.
   */
  private final Integer substringSize = 4;

  // calibration
  /**
   * The maximum and minimum number of RFs to consider.
   */
  private final Integer maxRF = 32;
  private final Integer minRF = 4;
  /**
   * The maximum number of records to sample.
   */
  private final Integer maxRecords = 100;
  /**
   * The maximum number of records to parse.
   */
  private final Integer parserMeasurementSamples = 10;
}
