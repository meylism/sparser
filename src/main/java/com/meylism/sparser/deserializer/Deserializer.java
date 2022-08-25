package com.meylism.sparser.deserializer;

public interface Deserializer {
  /**
   *
   * @param data data to be parsed
   * @return deserialized object
   * @throws Exception
   */
  Object deserialize(Object data) throws Exception;
}
