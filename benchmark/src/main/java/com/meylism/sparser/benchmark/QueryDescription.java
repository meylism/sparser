package com.meylism.sparser.benchmark;

import com.meylism.sparser.core.FileFormat;
import com.meylism.sparser.core.deserializer.Deserializer;
import com.meylism.sparser.core.predicate.Predicate;

import java.io.File;

public interface QueryDescription {
  String getName();

  String getQueryDescription();

  Predicate getQuery();

  FileFormat getFileFormat();

  Reader getReader(File file);

  Deserializer getDeserializer();
}
