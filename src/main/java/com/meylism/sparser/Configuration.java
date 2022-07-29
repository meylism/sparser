package com.meylism.sparser;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Configuration {
  private FileFormat fileFormat;

  // raw filter compilation
  public final static Integer SUBSTRING_SIZE = 4;

  // calibration
  /**
   * The maximum and minimum number of RFs to consider.
   */
  public static final Integer MAX_RF = 32;
  public static final Integer MIN_RF = 4;
  /**
   * The maximum number of records to sample.
   */
  public static final Integer MAX_RECORDS = 100;
  /**
   * The maximum number of records to parse.
   */
  public static final Integer PARSER_MEASUREMENT_SAMPLES = 10;
}
