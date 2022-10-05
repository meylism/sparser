package com.meylism.sparser.benchmark.json;

import com.meylism.sparser.benchmark.QueryDescription;
import com.meylism.sparser.benchmark.Reader;
import com.meylism.sparser.core.FileFormat;
import com.meylism.sparser.core.deserializer.Deserializer;

import java.io.File;
import java.io.FileNotFoundException;

public abstract class DefaultJsonQueryDescription implements QueryDescription {

  @Override public FileFormat getFileFormat() {
    return FileFormat.JSON;
  }

  @Override public Reader getReader(File file) {
    return new JsonReader(file);
  }

  @Override public Deserializer getDeserializer() {
    return new JacksonDeserializer();
  }
}
