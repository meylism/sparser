package com.meylism.sparser.optimization;

import com.google.common.collect.Sets;
import com.meylism.sparser.Configuration;
import com.meylism.sparser.TimeUtils;
import com.meylism.sparser.deserializer.Deserializer;
import com.meylism.sparser.filter.Cascade;
import com.meylism.sparser.operator.FilterOperator;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * Uses a cost model to calculate the cost for each filter in the cascade, and chooses a combination with the least
 * cost.
 */
public class CostBasedOptimizer extends Optimizer {
  private static final Logger logger = LogManager.getLogger(CostBasedOptimizer.class);

  private final Deserializer deserializer;

  // cascade
  public List<FilterOperator> bestCascade;

  // calibration stats
  private final Map<FilterOperator, FilterOptimizationContext> statsMap;
  int samplesProcessed = 0;
  long parsedRecords = 0;
  private long avgParserRuntime = 0;
  private double bestCascadeCost = Double.MAX_VALUE;

  public CostBasedOptimizer(Configuration conf, Deserializer deserializer) {
    super(conf);
    this.deserializer = deserializer;
    statsMap = new HashMap<>();
  }

  public void optimize(Cascade cascade, List<Object> sample) {
    calculateTheCostOfFilters(sample, cascade);
    chooseTheBestCascade(cascade);
  }

  private void calculateTheCostOfFilters(List<Object> sample, Cascade cascade) {
    final int NUM_OF_RECORDS = Math.min(Configuration.MAX_RECORDS, sample.size());
    final int NUM_OF_RECORDS_TO_PARSE = Math.min(NUM_OF_RECORDS, Configuration.PARSING_LIMIT_IN_OPTIMIZATION);

    for (Set<FilterOperator> conjunction : cascade.getCascade()) {
      for (FilterOperator filter : conjunction) {
        FilterOptimizationContext stats = new FilterOptimizationContext();
        stats.initPassthroughMask(NUM_OF_RECORDS);
        samplesProcessed = 0;

        for (Object record : sample) {
          long rfStart = TimeUtils.timeStart();
          boolean result = filter.evaluate(record);
          long timeElapsed = TimeUtils.timeStop(rfStart);
          stats.setAvgRuntime(stats.getAvgRuntime() + timeElapsed);

          if (result)
            stats.getPassthroughMask().set(samplesProcessed);

          if (parsedRecords <= NUM_OF_RECORDS_TO_PARSE) {
            long parseStart = TimeUtils.timeStart();

            try {
              Object object = this.deserializer.deserialize(record);
            } catch (Exception e) {
              throw new RuntimeException("Exception while parsing record");
            }

            long parseTimeElapsed = TimeUtils.timeStop(parseStart);
            avgParserRuntime += parseTimeElapsed;
            parsedRecords++;
          }

          samplesProcessed++;
          if (samplesProcessed > NUM_OF_RECORDS)
            break;

        }
        stats.setAvgRuntime(stats.getAvgRuntime() / samplesProcessed);
        statsMap.put(filter, stats);

      }
    }
    avgParserRuntime = avgParserRuntime / parsedRecords;
  }

  private void chooseTheBestCascade(Cascade cascade) {
    Set<List<FilterOperator>> combinations = Sets.cartesianProduct(cascade.getCascade());
    for (List<FilterOperator> combination : combinations) {
      FilterOperator firstFilter = combination.get(0);
      FilterOptimizationContext stat = statsMap.get(firstFilter);
      BitSet joint = stat.getPassthroughMask();
      // first RF runs unconditionally
      double totalCost = stat.getAvgRuntime();

      for (int i = 1; i < combination.size(); i++) {
        FilterOperator ithFilter = combination.get(i);
        FilterOptimizationContext iThStat = statsMap.get(ithFilter);
        long rfCost = iThStat.getAvgRuntime();
        int jointRate = joint.cardinality();
        double rate = (double) jointRate / (double) samplesProcessed;
        totalCost += rfCost * rate;
        joint.and(iThStat.getPassthroughMask());
      }

      int jointRate = joint.cardinality();
      double rate = (double) jointRate / (double) samplesProcessed;
      totalCost += rate * avgParserRuntime;

      logger.debug("Considering the schedule {}, cost {}", combination, totalCost);

      if (totalCost < bestCascadeCost) {
        logger.debug("The best schedule for now {}", combination);
        bestCascadeCost = totalCost;
        bestCascade = combination;
      }
    }
    modifyTheCascade(bestCascade, cascade);
  }


  private void modifyTheCascade(List<FilterOperator> bestCascade, Cascade originalCascade) {
    originalCascade.getCascade().clear();

    for (FilterOperator filter : bestCascade)
      originalCascade.addConjunction(new HashSet<>(Arrays.asList(filter)));
  }

  /**
   * Each {@link FilterOperator} is associated with a {@link FilterOptimizationContext} that contains
   * data-dependent parameters and is used while choosing the best cascade.
   */
  private static class FilterOptimizationContext {
    @Getter @Setter private long avgRuntime;
    /**
     * The ith bit tells whether the ith record passed this filter or not.
     */
    @Getter @Setter private BitSet passthroughMask;

    public void initPassthroughMask(int size) {
      this.passthroughMask = new BitSet(size);
    }
  }
}
