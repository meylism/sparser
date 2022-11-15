package com.meylism.sparser.benchmark.json;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonpCharacterEscapes;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meylism.sparser.core.deserializer.Deserializer;

public class JacksonDeserializer implements Deserializer {
  private ObjectMapper mapper;

  public JacksonDeserializer() {
    JsonFactory f = new JsonFactory();
    f.setCharacterEscapes(JsonpCharacterEscapes.instance());
    this.mapper = new ObjectMapper(f);
  }

  @Override public Object deserialize(Object data) throws Exception {
    return mapper.readTree((String) data);
  }
}
