package com.meylism.sparser.benchmark;

import com.meylism.sparser.core.FileFormat;
import com.meylism.sparser.core.deserializer.Deserializer;
import com.meylism.sparser.core.predicate.Predicate;

public interface QueryDescription {
  String getName();

  String getQueryAsString();

  Predicate getQuery();

  FileFormat getFileFormat();

  Class<? extends Reader> getReader();

  Dataset getDataset();

  Class<? extends Deserializer> getDeserializer();
}
