package com.meylism.sparser;

import com.meylism.sparser.predicate.ConjunctiveClause;
import com.meylism.sparser.predicate.SimplePredicate;
import com.meylism.sparser.rf.RawFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.BitSet;

public class Calibration {
  private static Logger logger = LogManager.getLogger(Calibration.class);
  /**
   * The maximum cascade depth.
   */
  private static final Integer MAX_SCHEDULE_SIZE = 32;
  private static final Integer MIN_SCHEDULE_SIZE = 4;

  /**
   * The maximum number of records to sample.
   */
  private static final Integer MAX_RECORDS = 100;
  /**
   * The maximum number of records to parse.
   */
  private static final Integer PARSER_MEASUREMENT_SAMPLES = 10;

  private Deserializer deserializer;
  private ArrayList<ConjunctiveClause> clauses;

  // cascade
  public ArrayList<RawFilter> bestCascade = new ArrayList<>();
  public ArrayList<Integer> bestCascadeSources = new ArrayList<>();
  private double bestCost;

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

//  private int numSampled = 0;
//  private double parseCost = 0;
//  private double bestCost = Double.MAX_VALUE;
//  private int[] bestCascade;
//  private int bestCascadeLen;

  /**
   *
   * @param clauses a list of conjunctive clauses
   * @param deserializer - deserializer that implements Deserializer. Defaults to Jackson
   */
  public Calibration(final ArrayList<ConjunctiveClause> clauses, final Deserializer deserializer) {
    this.clauses = clauses;
    this.deserializer = deserializer;
    this.calculateTotalNumberOfRFs();
    assert totalNumberOfRFs > 0;
  }

  /**
   * Get the data- and format-dependent parameters from the samples provided.
   *
   * @param samples
   */
  public void calibrate(ArrayList<String> samples) throws Exception {
    final int NUM_OF_RECORDS = Math.min(MAX_RECORDS, samples.size());
    if (totalNumberOfRFs > MAX_SCHEDULE_SIZE) {
      // todo: select 32 by picking a random RF generated from each token in a round-robin fashion
    } else {
      // todo: otherwise: max(clauses, 4)
    }
    final int CASCADE_DEPTH = this.clauses.size();

    final int NUM_OF_RECORDS_TO_PARSE = Math.min(NUM_OF_RECORDS, PARSER_MEASUREMENT_SAMPLES);

    for (ConjunctiveClause clause : clauses) {

      for (SimplePredicate predicate : clause.getSimplePredicates()) {

        for (RawFilter rawFilter : predicate.getRawFilters()) {

          rawFilter.initPassthroughMask(NUM_OF_RECORDS);
          samplesProcessed = 0;
          for (String sample : samples) {
            long rfStart = TimeUtils.timeStart();
            boolean result = rawFilter.evaluate(sample);
            long timeElapsed = TimeUtils.timeStop(rfStart);
            rawFilter.addToExecutionTime(timeElapsed);

            if (result)
              rawFilter.maskSetBit(samplesProcessed);

            if (parsedRecords <= NUM_OF_RECORDS_TO_PARSE) {
              long parseStart = TimeUtils.timeStart();
              Object object = this.deserializer.deserialize(sample);
              long parseTimeElapsed = TimeUtils.timeStop(parseStart);
              assert object != null;
              avgParserRuntime += parseTimeElapsed;
              parsedRecords++;
            }

            samplesProcessed++;
            if (samplesProcessed > NUM_OF_RECORDS)
              break;

          }
          rawFilter.calculateAvgExecutionTime(samplesProcessed);
        }
      }
    }
    avgParserRuntime = avgParserRuntime / parsedRecords;

    populateRFsTogether();
    for (int i=0; i<CASCADE_DEPTH; i++) {
      bestCascade.add(null);
      bestCascadeSources.add(null);
    }
    for (int depth=1; depth<=CASCADE_DEPTH; depth++) {
      bestCost = Double.MAX_VALUE;
      chooseTheBestCascade(depth, 0, depth);
    }
  }

  private void chooseTheBestCascade(int currentCascadeDepth, int start, int cascadeDepth) {
    // base case
    if (currentCascadeDepth == 0) {
      for (int i=0; i<cascadeDepth; i++)
        System.out.print(bestCascade.get(i));
      System.out.println();

      for (int i=0; i<cascadeDepth; i++) {
        for (int j=0; j<cascadeDepth; j++) {
          if (i != j && bestCascadeSources.get(i) == bestCascadeSources.get(j))
            return;
        }
      }

      RawFilter firstrRF = bestCascade.get(0);
      BitSet joint = firstrRF.getPassthroughMask();
      // first RF runs unconditionally
      double totalCost = firstrRF.getAvgRuntimeCost();

      for (int i=1; i<cascadeDepth; i++) {
        RawFilter rf = bestCascade.get(i);
        long rfCost = rf.getAvgRuntimeCost();
        int jointRate = joint.cardinality();
        double rate = (double)jointRate / (double)samplesProcessed;
        totalCost += rfCost * rate;
        joint.and(rf.getPassthroughMask());
      }

      int jointRate = joint.cardinality();
      double rate = (double) jointRate / (double) samplesProcessed;
      totalCost += rate * avgParserRuntime;

      if (totalCost < bestCost) {
        bestCost = totalCost;
      }

      return;
    }

    for (int i = start; i< rawFilters.size()-currentCascadeDepth; i++) {
          bestCascade.set(cascadeDepth-currentCascadeDepth, rawFilters.get(i));
          bestCascadeSources.set(cascadeDepth-currentCascadeDepth, sourceClauses.get(i));
          chooseTheBestCascade(currentCascadeDepth-1, i+1, cascadeDepth);
    }

  }

  private void populateRFsTogether() {
    for (int i=0; i<clauses.size(); i++) {
      ArrayList<SimplePredicate> predicates = clauses.get(i).getSimplePredicates();
      for (int j=0; j<predicates.size(); j++) {
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
   * Get the total number of RFs available in the whole query predicate.
   * @return
   */
  private void calculateTotalNumberOfRFs() {
    int sum = 0;
    for (ConjunctiveClause clause : clauses)
      sum += clause.getTotalNumberOfRFs();
    this.totalNumberOfRFs =  sum;
  }
}
