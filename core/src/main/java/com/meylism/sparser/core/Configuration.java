package com.meylism.sparser.core;

import lombok.Getter;
import lombok.Setter;

public class Configuration {

  // raw filter compilation
  /**
   * The size of to-be generated tokens for RFs.
   */
  public static final Integer SUBSTRING_SIZE = 4;

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
  public static final Integer PARSING_LIMIT_IN_OPTIMIZATION = 10;

  // general
  @Getter @Setter private FileFormat fileFormat;
}
