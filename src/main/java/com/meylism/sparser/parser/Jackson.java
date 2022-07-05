package com.meylism.sparser.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meylism.sparser.Parser;

public class Jackson implements Parser {
  private ObjectMapper objectMapper;

  public Jackson() {
    objectMapper = new ObjectMapper();
  }

  @Override public Object deserialize(String record) throws JsonProcessingException {
    return objectMapper.readTree(record);
  }
}
