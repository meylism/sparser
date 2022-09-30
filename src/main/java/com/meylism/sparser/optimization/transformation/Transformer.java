package com.meylism.sparser.optimization.transformation;

import com.meylism.sparser.Configuration;
import com.meylism.sparser.predicate.Predicate;

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
