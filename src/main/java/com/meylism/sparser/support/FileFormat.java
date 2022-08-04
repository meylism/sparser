package com.meylism.sparser.support;

import com.meylism.sparser.deserializer.Deserializer;
import com.meylism.sparser.deserializer.JacksonDeserializer;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

public enum FileFormat {
  JSON (
      new FilterSupport(PredicateSupport.EXACT_STRING_MATCH, RawFilterSupport.UTF_SUBSTRING_SEARCH)
  ) {
    @Override
    Deserializer initDeserializer() {
      return new JacksonDeserializer();
    }
  };

  @Getter
  private Deserializer deserializer;
  @Getter
  private List<FilterSupport> supportedFilters;

  FileFormat(FilterSupport... supportedFilters) {
    this.supportedFilters = Arrays.asList(supportedFilters);
    this.deserializer = initDeserializer();
  }

  /**
   * Deserializer to be used while calibrating.
   *
   * @return Deserializer
   */
  abstract Deserializer initDeserializer();
}
