package com.meylism.sparser.operator;

import lombok.Getter;

import java.util.List;

/**
 * Base class for Key-Value Match Filter Operator.
 * This filter tries to find the co-occurrence of key and value.
 */
public abstract class KeyValueMatchOperator extends FilterOperator {
  @Getter private final String key;
  @Getter private final String value;
  @Getter private final List<Character> delimiters;

  protected KeyValueMatchOperator(String key, String value, List<Character> delimiters) {
    this.key = key;
    this.value = value;
    this.delimiters = delimiters;

  }
}
