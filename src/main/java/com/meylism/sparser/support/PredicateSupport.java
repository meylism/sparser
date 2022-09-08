package com.meylism.sparser.support;

/**
 * An enumeration of supported predicates in Sparser.
 * Check out README for more info.
 */
public enum PredicateSupport {
  /**
   * Predicate: Exact String Match
   */
  EXACT_MATCH,
  /**
   * Predicate: Key-Value Match
   */
  KEY_VALUE_MATCH,
  /**
   * Predicate: Contains String
   */
  CONTAINS_STRING,
  /**
   * Predicate: Contains Key
   */
  CONTAINS_KEY
}
