package com.meylism.sparser;

import com.meylism.sparser.deserializer.Deserializer;
import com.meylism.sparser.deserializer.JacksonDeserializer;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

enum FileFormat {
  JSON(
      new FilterSupport(Predicate.EXACT_STRING_MATCH, RawFilter.UTF_SUBSTRING_SEARCH),
      new FilterSupport(Predicate.EXACT_STRING_MATCH, RawFilter.UTF_KEY_VALUE_SEARCH)
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
