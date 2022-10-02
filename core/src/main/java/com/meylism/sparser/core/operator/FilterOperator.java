package com.meylism.sparser.core.operator;

import lombok.Getter;
import lombok.Setter;

/**
 * Base class for Sparser's filter primitives.
 */
public abstract class FilterOperator {
  @Getter @Setter private Boolean inverted = false;

  public abstract Boolean evaluate(Object record);
}
