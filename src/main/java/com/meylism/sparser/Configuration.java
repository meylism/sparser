package com.meylism.sparser;

import com.meylism.sparser.calibration.Calibrator;
import com.meylism.sparser.deserializer.Deserializer;
import com.meylism.sparser.predicate.ConjunctiveClause;
import com.meylism.sparser.rf.compiler.RawFilterCompiler;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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
  public static final Integer PARSER_MEASUREMENT_SAMPLES = 10;

  /**
   * Deserializer to be used while calibrating.
   */
  @Getter @Setter private Deserializer deserializer;

  // general
  @Getter @Setter private FileFormat fileFormat;
  @Getter @Setter private Calibrator calibrator;
  @Getter @Setter private RawFilterCompiler rawFilterCompiler;
  @Getter @Setter private List<ConjunctiveClause> clauses;


}
