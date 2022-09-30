package com.meylism.sparser.operator.compiler;

import com.meylism.sparser.Configuration;
import com.meylism.sparser.predicate.Predicate;
import lombok.Getter;

/**
 * Responsible for the compilation of filtering primitives from the given predicates.
 */
public abstract class RawFilterCompiler {
  @Getter private Configuration configuration;

  protected RawFilterCompiler(Configuration configuration) {
    this.configuration = configuration;
  }
  public abstract void compile(Predicate predicate);
}
