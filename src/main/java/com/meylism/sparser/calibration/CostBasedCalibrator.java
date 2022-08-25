package com.meylism.sparser.calibration;

import com.meylism.sparser.Configuration;
import com.meylism.sparser.TimeUtils;
import com.meylism.sparser.filter.Filter;
import com.meylism.sparser.predicate.ConjunctiveClause;
import com.meylism.sparser.predicate.SimplePredicate;
import com.meylism.sparser.rf.RawFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class CostBasedCalibrator extends Calibrator {
  private static Logger logger = LogManager.getLogger(CostBasedCalibrator.class);

  // cascade
  public List<RawFilter> bestCascade;
  private ArrayList<RawFilter> tempCascade = new ArrayList<>();
  private ArrayList<Integer> tempCascadeIndices = new ArrayList<>();
  private double bestCost = Double.MAX_VALUE;

  // sources
  private ArrayList<Integer> sourceClauses = new ArrayList<>();
  private ArrayList<Integer> sourcePredicates = new ArrayList<>();
  private ArrayList<RawFilter> rawFilters = new ArrayList<>();

  // time
  private long avgParserRuntime = 0;
  private int totalNumberOfRFs = 0;

  // calibration stats
  int samplesProcessed = 0;
  long parsedRecords = 0;

  public CostBasedCalibrator(Configuration conf, Filter filter) {
    super(conf, filter);
    this.calculateTotalNumberOfRFs();
    assert totalNumberOfRFs > 0;
  }

  /**
   * Get the data- and format-dependent parameters from the samples provided.
   *
   * @param samples
   */
  public void calibrate(List samples) throws Exception {
    final int NUM_OF_RECORDS = Math.min(Configuration.MAX_RECORDS, samples.size());

    if (totalNumberOfRFs > Configuration.MAX_RF) {
      // if the number of RFs is greater than MAX_RF, select MAX_RF RFs randomly in a round-robin fashion
      // todo: select 32 by picking a random RF generated from each token in a round-robin fashion
    }

    final int CASCADE_DEPTH = this.configuration.getClauses().size();

    final int NUM_OF_RECORDS_TO_PARSE = Math.min(NUM_OF_RECORDS, Configuration.PARSER_MEASUREMENT_SAMPLES);

    for (ConjunctiveClause clause : this.configuration.getClauses()) {

      for (SimplePredicate predicate : clause.getSimplePredicates()) {

        for (RawFilter rawFilter : predicate.getRawFilters()) {

          rawFilter.initPassthroughMask(NUM_OF_RECORDS);
          samplesProcessed = 0;
          for (Object sample : samples) {
            long rfStart = TimeUtils.timeStart();
            boolean result = rawFilter.evaluate(sample);
            long timeElapsed = TimeUtils.timeStop(rfStart);
            rawFilter.setAvgRuntime(rawFilter.getAvgRuntime() + timeElapsed);

            if (result)
              rawFilter.setPassthroughMaskBit(samplesProcessed);

            if (parsedRecords <= NUM_OF_RECORDS_TO_PARSE) {
              long parseStart = TimeUtils.timeStart();
              Object object = configuration.getDeserializer().deserialize(sample);
              long parseTimeElapsed = TimeUtils.timeStop(parseStart);
              assert object != null;
              avgParserRuntime += parseTimeElapsed;
              parsedRecords++;
            }

            samplesProcessed++;
            if (samplesProcessed > NUM_OF_RECORDS)
              break;

          }
          rawFilter.setAvgRuntime(rawFilter.getAvgRuntime() / samplesProcessed);
        }
      }
    }
    avgParserRuntime = avgParserRuntime / parsedRecords;

    populateRFsTogether();
    for (int i = 0; i < CASCADE_DEPTH; i++) {
      tempCascade.add(null);
      tempCascadeIndices.add(null);
    }

    chooseTheBestCascade(CASCADE_DEPTH, 0, CASCADE_DEPTH);
    assert bestCascade.size() == CASCADE_DEPTH;
  }

  @Override
  public Filter getFilter() {
    return filter;
  }

  /**
   * Recursively generate combinations of RFs and choose the best one.
   *
   * @param currentCascadeDepth current depth of the cascade in the recursive procedure
   * @param start
   * @param cascadeDepth depth of the resulting cascade
   */
  private void chooseTheBestCascade(int currentCascadeDepth, int start, int cascadeDepth) {
    // base case
    if (currentCascadeDepth == 0) {
      // skip cascades if two RFs belong to the same conjunctive clause
      // in other words only consider a single RF from each conjunctive clause
      for (int i = 0; i < cascadeDepth; i++) {
        for (int j = 0; j < cascadeDepth; j++) {
          if (i != j && sourceClauses.get(tempCascadeIndices.get(i)) == sourceClauses.get(tempCascadeIndices.get(j))) {
            logger.debug("Skipping the schedule {}", tempCascade.subList(0, cascadeDepth));
            return;
          }
        }
      }

      RawFilter firstRF = tempCascade.get(0);
      BitSet joint = firstRF.getPassthroughMask();
      // first RF runs unconditionally
      double totalCost = firstRF.getAvgRuntime();

      for (int i = 1; i < cascadeDepth; i++) {
        RawFilter rf = tempCascade.get(i);
        long rfCost = rf.getAvgRuntime();
        int jointRate = joint.cardinality();
        double rate = (double) jointRate / (double) samplesProcessed;
        totalCost += rfCost * rate;
        joint.and(rf.getPassthroughMask());
      }

      int jointRate = joint.cardinality();
      double rate = (double) jointRate / (double) samplesProcessed;
      totalCost += rate * avgParserRuntime;

      logger.debug("Considering the schedule {}, cost {}", tempCascade.subList(0, cascadeDepth), totalCost);

      if (totalCost < bestCost) {
        logger.debug("The best schedule for now {}", tempCascade.subList(0, cascadeDepth));
        bestCost = totalCost;
        bestCascade = new ArrayList<>(tempCascade.subList(0, cascadeDepth));
      }

      return;
    }

    for (int i = start; i <= rawFilters.size() - currentCascadeDepth; i++) {
      tempCascade.set(cascadeDepth - currentCascadeDepth, rawFilters.get(i));
      tempCascadeIndices.set(cascadeDepth - currentCascadeDepth, i);
      chooseTheBestCascade(currentCascadeDepth - 1, i + 1, cascadeDepth);
    }
  }

  private void populateRFsTogether() {
    for (int i = 0; i < this.configuration.getClauses().size(); i++) {
      ArrayList<SimplePredicate> predicates = this.configuration.getClauses().get(i).getSimplePredicates();
      for (int j = 0; j < predicates.size(); j++) {
        ArrayList<RawFilter> rawFilters = predicates.get(j).getRawFilters();
        for (RawFilter rawFilter : rawFilters) {
          sourceClauses.add(i);
          sourcePredicates.add(j);
          this.rawFilters.add(rawFilter);
        }
      }
    }
  }

  private static double getRfCost(final int length) {
    return length * 8.0;
  }

  /**
   * Calculate the total number of RFs available in the whole query predicate.
   */
  private void calculateTotalNumberOfRFs() {
    int sum = 0;
    for (ConjunctiveClause clause : this.configuration.getClauses())
      sum += clause.getTotalNumberOfRFs();
    this.totalNumberOfRFs = sum;
  }

  @Override public List<RawFilter> getBestCascade() {
    return bestCascade;
  }
}
