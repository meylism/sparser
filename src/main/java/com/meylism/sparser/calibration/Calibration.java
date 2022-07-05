package com.meylism.sparser.calibration;

import com.meylism.sparser.Parser;
import com.meylism.sparser.Query;
import com.meylism.sparser.Utils;
import com.meylism.sparser.rf.AsciiRawFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class Calibration {
  private static Logger logger = LogManager.getLogger(Calibration.class);

  private static final Integer MAX_RAW_FILTERS = 32;
  private static final Integer MAX_SCHEDULE_SIZE = 4;
  private static final Integer MAX_RECORDS = 100;
  private static final Integer PARSER_MEASUREMENT_SAMPLES = 10;

  private ArrayList<BitSet> passthroughMasks;

  private int numSampled = 0;
  private double parseCost = 0;
  private double bestCost = Double.MAX_VALUE;
  private int[] bestCascade;
  private int bestCascadeLen;


  public Calibration() {
  }

  public Query calibrate(List<String> records, AsciiRawFilter asciiRawFilters, Parser parser) throws Exception {

    final int numOfRawFilters = asciiRawFilters.getRawFilters().size() > MAX_RAW_FILTERS ? MAX_RAW_FILTERS :
        asciiRawFilters.getRawFilters().size();

    final int numOfRecords = records.size() > MAX_RECORDS ? MAX_RECORDS : records.size();

    final int cascadeDepth = Math.max(asciiRawFilters.getRawFilters().size(), MAX_SCHEDULE_SIZE);

    passthroughMasks = new ArrayList<>(numOfRawFilters);

    // init bitmaps
    for (int i=0; i<numOfRawFilters; i++) {
      passthroughMasks.add(new BitSet(numOfRecords));
    }


    int recordIndex = 0;
    long parsedRecords = 0;
    long passed = 0;
    long parseCostTotal = 0;

    while (recordIndex < numOfRecords) {

      for (int i=0; i<numOfRawFilters; i++) {
        logger.debug("Grepping ", asciiRawFilters.getRawFilters().get(i));

        if (records.get(recordIndex).indexOf(asciiRawFilters.getRawFilters().get(i)) > -1) {
          passthroughMasks.get(i).set(recordIndex);
          logger.debug("found");
        } else {
          logger.debug("not found");
        }
      }

      if (recordIndex < PARSER_MEASUREMENT_SAMPLES) {
        long timeParse = Utils.timeStart();
        parser.deserialize(records.get(recordIndex));
        parseCostTotal += Utils.timeStop(timeParse);
        parsedRecords++;
      }
      recordIndex++;
    }

    parseCost = parseCostTotal / parsedRecords;
    numSampled = recordIndex;

    int result[] = new int[cascadeDepth];
    for (int i=1; i <= cascadeDepth; i++) {
      bestCost = Double.MAX_VALUE;
      searchBestCascade(asciiRawFilters, i, 0, result, i);
    }

    Query query = new Query();
    for (int i=0; i<bestCascadeLen; i++) {
      int index = bestCascade[i];
      query.add(asciiRawFilters.getRawFilters().get(index));
    }

    logger.debug(query);

    return query;
  }

  private void searchBestCascade(AsciiRawFilter arf, int len, int start, int result[], int resultLen) {
    if (len == 0) {
      assert resultLen > 0;

      for (int i=0; i<resultLen; i++) {
        for (int j=0; j<resultLen; j++) {
          if (i != j && arf.getSources().get(result[i]) == arf.getSources().get(result[j])) {
            logger.debug("{}, {}, {}, {}", "Skipping due to duplicate source", arf.getRawFilters().get(result[i]), " ",
                arf.getRawFilters().get(result[j]));
            return;
          }
        }
      }

      int firstIndex = result[0];
      BitSet joint = (BitSet) passthroughMasks.get(firstIndex).clone();

      double totalCost = getRfCost(arf.getRawFilters().get(firstIndex).length());

      for (int i=1; i<resultLen; i++) {
        int index = result[i];
        int jointRate = joint.cardinality();
        double filterCost = getRfCost(arf.getRawFilters().get(index).length());
        double rate = (double)jointRate / numSampled;
        logger.debug("Rate after " + arf.getRawFilters().get(result[i-1]) + " " + rate);
        totalCost += filterCost * rate;

        joint.and(passthroughMasks.get(i));
      }

      int jointRate = joint.cardinality();
      double rate = (double)jointRate / numSampled;
      logger.debug("Rate after(full parse) " + arf.getRawFilters().get(result[resultLen-1]) + " " + rate);
      totalCost += parseCost * rate;

      if (totalCost < bestCost) {
        assert resultLen <= MAX_SCHEDULE_SIZE;
        bestCascade = result.clone();
        bestCost = totalCost;

        for (int i=0; i<resultLen; i++)
          System.out.println(arf.getRawFilters().get(result[i]));
       System.out.println(totalCost);

        bestCascadeLen = resultLen;
      }

      return;
    }


    for (int i = start; i <= arf.getRawFilters().size()-len; i++) {
      result[resultLen-len] = i;
      searchBestCascade(arf, len-1, i+1, result, resultLen);
    }
  }

  private static double getRfCost(final int length) {
    return length * 8.0;
  }
}
