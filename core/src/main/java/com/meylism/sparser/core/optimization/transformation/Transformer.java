package com.meylism.sparser.core.optimization.transformation;

import com.meylism.sparser.core.Configuration;
import com.meylism.sparser.core.predicate.Predicate;

/**
 * A transformer applies certain transforms/changes to the given predicate in-place.
 */
public abstract class Transformer {
  private Configuration configuration;

  public Transformer(Configuration configuration) {
    this.configuration = configuration;
  }

  public abstract void transform(Predicate predicate);
}
