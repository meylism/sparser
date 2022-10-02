package com.meylism.sparser.core.operator;

import lombok.Getter;
import lombok.ToString;

/**
 * Base class for Exact Match Filter primitive.
 * Tries to find whether a token exists in the whole record.
 */
@ToString public abstract class ExactMatchFilterOperator extends FilterOperator {
  @Getter private final String value;

  protected ExactMatchFilterOperator(final String value) {
    this.value = value;
  }
}
