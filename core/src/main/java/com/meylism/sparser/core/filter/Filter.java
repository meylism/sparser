package com.meylism.sparser.core.filter;

import com.meylism.sparser.core.Configuration;
import com.meylism.sparser.core.operator.FilterOperator;
import com.meylism.sparser.core.optimization.Optimizer;
import com.meylism.sparser.core.optimization.transformation.Transformer;
import com.meylism.sparser.core.predicate.Predicate;
import com.meylism.sparser.core.predicate.SimplePredicate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Filter {
  private static final Logger logger = LogManager.getLogger(Filter.class);

  private final Configuration configuration;
  private final Predicate predicate;
  //  private OptimizationHandler optimizationHandler;
  private final List<Transformer> transformers;
  private final List<Optimizer> optimizers;
  private final List<Object> sampleForOptimization;
  private Cascade cascade;
  // general
  private int numOfRecordsTillNow;
  // optimization
  private boolean optimizationNeeded = true;
  private int numberOfSamplesTillNow;

  /**
   *
   * @param configuration
   * @param predicate
   */
  public Filter(Configuration configuration, Predicate predicate, List<Transformer> transformers,
      List<Optimizer> optimizers) {
    this.configuration = configuration;
    this.predicate = predicate;
    this.transformers = transformers;
    this.optimizers = optimizers;
    //    this.optimizationHandler = optimizationHandler;
    sampleForOptimization = new ArrayList<>();
    cascade = new Cascade();
    applyTransformers();
  }

  /**
   * Apply filter on the given record.
   *
   * @param record
   * @return true if record can be filtered
   */
  public Boolean filter(Object record) {
    numOfRecordsTillNow += 1;

    if (optimizationNeeded) {
      handleOptimization(record);
      return false;
    }

    return applyFilter(record);
  }

  private Boolean applyFilter(Object record) {
    int passesInConjunction;
    for (Set<FilterOperator> conjunction : cascade.getCascade()) {
      passesInConjunction = 0;
      for (FilterOperator operator : conjunction) {
        if (operator.evaluate(record))
          passesInConjunction++;
      }
      if (passesInConjunction == conjunction.size())
        return false;
    }
    return true;
  }

  private void handleOptimization(Object record) {
    if (numberOfSamplesTillNow < Configuration.MAX_RECORDS) {
      numberOfSamplesTillNow++;
      sampleForOptimization.add(record);
      return;
    }

    applyOptimizers();

    sampleForOptimization.clear();
    optimizationNeeded = false;
    numberOfSamplesTillNow = 0;
  }

  /**
   * Initial cascade is just a
   */
  private void initInitialCascade(Predicate predicate, Cascade cascade) {
    switch (predicate.getType()) {
    case AND:
      Set<FilterOperator> conjunctionOfFilters = new HashSet<>();
      for (Predicate child : predicate.getChildren())
        conjunctionOfFilters.addAll(((SimplePredicate) child).getRawFilters());
      cascade.addConjunction(conjunctionOfFilters);
      break;
    case OR:
      for (Predicate child : predicate.getChildren())
        initInitialCascade(child, cascade);
      break;
    default:
      cascade.addConjunction(((SimplePredicate) predicate).getRawFilters());
    }
  }

  private void applyTransformers() {
    logger.debug("Applying transformers:");
    for (Transformer transformer : this.transformers) {
      logger.debug("Starting {}.", transformer.getClass().getSimpleName());
      transformer.transform(this.predicate);
      logger.debug("{} applied.", transformer.getClass().getSimpleName());

    }
    initInitialCascade(this.predicate, this.cascade);
  }

  private void applyOptimizers() {
    logger.debug("(Re)Applying optimizers:");
    for (Optimizer optimizer : this.optimizers) {
      logger.debug("Starting {}.", optimizer.getClass().getSimpleName());
      optimizer.optimize(this.cascade, this.sampleForOptimization);
      logger.debug("{} applied.", optimizer.getClass().getSimpleName());
    }
  }
}
