package com.meylism.sparser.optimization;

import com.meylism.sparser.Configuration;
import com.meylism.sparser.filter.Cascade;
import lombok.Getter;

import java.util.List;

/**
 * While {@link com.meylism.sparser.optimization.transformation.Transformer transformers} optimize the provided
 * predicate, {@link Optimizer optimizers} optimize the cascade that was drawn from the predicate in an attempt to
 * make filtering faster.
 *
 * Each optimizer is provided with a sample of records being filtering so that they can take data-dependent
 * parameters into account while optimizing.
 *
 * Moreover, in Sparser there's a concept of re-optimization; if Sparser notices the filtering performance is
 * degrading, it re-runs the optimizations but with a different sample of records.
 */
public abstract class Optimizer {
  @Getter protected Configuration configuration;

  protected Optimizer(Configuration configuration) {
    this.configuration = configuration;
  }

  /**
    Optimize.

   * @param cascade current state of cascade
   * @param sample a sample of records
   */
  public abstract void optimize(Cascade cascade, List<Object> sample);
}
