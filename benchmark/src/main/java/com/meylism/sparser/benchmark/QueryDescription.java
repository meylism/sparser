package com.meylism.sparser.benchmark;

import com.meylism.sparser.core.FileFormat;
import com.meylism.sparser.core.deserializer.Deserializer;
import com.meylism.sparser.core.predicate.Predicate;

import java.io.File;

public interface QueryDescription {
  // Queries are identified by their names and are therefore expected to be unique across the project.
  // You will get warned if queries share a name together.
  String getName();

  String getQueryDescription();

  Predicate getQuery();

  FileFormat getFileFormat();

  Reader getReader(File file);

  Deserializer getDeserializer();
}
