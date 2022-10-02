package com.meylism.sparser.core;

import com.google.common.base.Preconditions;
import com.meylism.sparser.core.deserializer.Deserializer;
import com.meylism.sparser.core.filter.Filter;
import com.meylism.sparser.core.operator.compiler.RawFilterCompiler;
import com.meylism.sparser.core.operator.compiler.RuleBasedRawFilterCompiler;
import com.meylism.sparser.core.optimization.CostBasedOptimizer;
import com.meylism.sparser.core.optimization.Optimizer;
import com.meylism.sparser.core.optimization.transformation.FilterPruningTransformer;
import com.meylism.sparser.core.optimization.transformation.Transformer;
import com.meylism.sparser.core.predicate.Predicate;

import java.util.Arrays;
import java.util.List;

/**
 * A facade for Sparser.
 */
public class Sparser {
  private final Configuration configuration;
  private final Predicate predicate;
  private final Deserializer deserializer;
  private Filter filter;

  /**
   *
   * @param predicate query predicate in DNF(disjunctive normal form)
   * @param fileFormat format of the data being filtered
   * @param deserializer deserializer that will be used while optimizing Sparser
   */
  public Sparser(Predicate predicate, FileFormat fileFormat, Deserializer deserializer) {
    Preconditions.checkNotNull(predicate, "predicate");
    Preconditions.checkNotNull(fileFormat, "fileformat");
    Preconditions.checkNotNull(deserializer, "deserializer");

    configuration = new Configuration();
    this.predicate = predicate;
    configuration.setFileFormat(fileFormat);
    this.deserializer = deserializer;
    compile();
    this.filter = initFilter();
  }

  /**
   * Apply filter on the given record.
   *
   * @return true if record can be filtered
   */
  public Boolean filter(Object record) {
    return filter.filter(record);
  }

  /**
   * Compile possible filtering primitives from the given query predicate.
   * The function expects the query predicate to be in disjunctive normal form(i.e., of the form (a AND b) OR (c AND
   * …) OR … ).
   */
  private void compile() {
    //    if (!PredicateUtils.isInDnf(predicate)) throw new RuntimeException("The query doesn't seem to be in DNF");
    RawFilterCompiler filterCompiler = new RuleBasedRawFilterCompiler(this.configuration);
    filterCompiler.compile(predicate);
  }

  private Filter initFilter() {
    return new Filter(configuration, predicate, initTransformers(), initOptimizers());
  }

  private List<Transformer> initTransformers() {
    return Arrays.asList(new FilterPruningTransformer(this.configuration));
  }

  private List<Optimizer> initOptimizers() {
    return Arrays.asList(new CostBasedOptimizer(this.configuration, this.deserializer));
  }
}
