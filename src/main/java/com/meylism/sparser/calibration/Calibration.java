package com.meylism.sparser.calibration;

import com.meylism.sparser.Parser;
import com.meylism.sparser.Utils;
import com.meylism.sparser.rf.AsciiRawFilter;
import jdk.jshell.execution.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class Calibration {
  private static Logger logger = LogManager.getLogger(Calibration.class);

  static final Integer MAX_RAW_FILTERS = 32;
  static final Integer MAX_RECORDS = 1000;
  static final Integer PARSER_MEASUREMENT_SAMPLES = 10;

  private static Timing calibrateTiming = new Timing();

  public static void calibrate(List<String> records, List<AsciiRawFilter> asciiRawFilters, Parser parser) {
    long timeEndToEnd = Utils.timeStart();

    ArrayList<BitSet> passthroughMasks = new ArrayList<>(MAX_RAW_FILTERS);
    for (int i=0; i<MAX_RAW_FILTERS; i++) {
      passthroughMasks.add(new BitSet(MAX_RECORDS));
    }

    int numOfRawFilters = asciiRawFilters.size() > MAX_RAW_FILTERS ? MAX_RAW_FILTERS : asciiRawFilters.size();

    int recordIndex = 0;
    long parsedRecords = 0;
    long passed = 0;
    long parseCost = 0;

    long timeStart = Utils.timeStart();

    while (recordIndex < MAX_RECORDS && recordIndex < records.size()) {

      long timeGrep = Utils.timeStart();

      for (int i=0; i<numOfRawFilters; i++) {
        logger.debug("Grepping ", asciiRawFilters.get(i));

        if (records.get(recordIndex).indexOf(asciiRawFilters.get(i).getRawFilter()) > -1) {
          passthroughMasks.get(i).set(recordIndex);
          logger.debug("found");
        } else {
          logger.debug("not found");
        }
      }
      long timeGrepElapsed = Utils.timeStop(timeGrep);
      calibrateTiming.setGreppingTotal(calibrateTiming.getGreppingTotal() + timeGrepElapsed);

      if (recordIndex < PARSER_MEASUREMENT_SAMPLES) {
        long timeParse = Utils.timeStart();
        parser.deserialize(records.get(recordIndex));
        long timeParseElapsed = Utils.timeStop(timeParse);
        parseCost += timeParseElapsed;
        parsedRecords++;
      }
      recordIndex++;
      calibrateTiming.setParseCost(parseCost);
    }

    long timeStartElapsed = Utils.timeStop(timeStart);
    calibrateTiming.setSamplingTotal(timeStartElapsed);

    timeStart = Utils.timeStart();
    logger.debug("Passed: ", passed);
    parseCost = parseCost / parsedRecords;

  }
}
