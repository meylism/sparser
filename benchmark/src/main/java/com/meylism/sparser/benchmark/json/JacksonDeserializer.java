package com.meylism.sparser.benchmark.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meylism.sparser.core.deserializer.Deserializer;

public class JacksonDeserializer implements Deserializer {
  private ObjectMapper mapper;

  public JacksonDeserializer() {
    this.mapper = new ObjectMapper();
  }

  @Override public Object deserialize(Object data) throws Exception {
    return mapper.readTree((String) data);
  }
}
