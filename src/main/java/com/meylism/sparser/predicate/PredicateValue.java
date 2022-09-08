package com.meylism.sparser.predicate;

import com.meylism.sparser.Configuration;
import com.meylism.sparser.FileFormat;
import lombok.Getter;

public class PredicateValue {
  private final Object value;

  public PredicateValue(Object value) {
    this.value = value;
  }

  public String getValue(Configuration configuration) {
    if (value instanceof String) {
      return (String) value;
    } else if (value instanceof Integer) {
      return ((Integer) value).toString();
    } else if (value instanceof Boolean) {
      return ((Boolean) value).toString();
    } else {
      throw new RuntimeException("this should never happen");
    }
  }
}
