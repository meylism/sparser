package com.meylism.sparser.deserializer;

public interface Deserializer {
  /**
   *
   * @param record record to be parsed
   * @return deserialized object
   * @throws Exception
   */
  Object deserialize(String record) throws Exception;
}