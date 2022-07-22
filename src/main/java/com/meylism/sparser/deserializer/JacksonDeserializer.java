package com.meylism.sparser.deserializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meylism.sparser.Deserializer;

public class JacksonDeserializer implements Deserializer {
  private ObjectMapper objectMapper;

  public JacksonDeserializer() {
    objectMapper = new ObjectMapper();
  }

  @Override public Object deserialize(String record) throws JsonProcessingException {
    return objectMapper.readTree(record);
  }
}
