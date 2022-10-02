package com.meylism.sparser.benchmark.json;

import com.meylism.sparser.benchmark.QueryDescription;
import com.meylism.sparser.benchmark.Reader;
import com.meylism.sparser.core.FileFormat;

public abstract class DefaultJsonQueryDescription implements QueryDescription {

  @Override public FileFormat getFileFormat() {
    return FileFormat.JSON;
  }

  @Override public Class<? extends Reader> getReader() {
    return JsonReader.class;
  }

  @Override public Class<JacksonDeserializer> getDeserializer() {
    return JacksonDeserializer.class;
  }
}
