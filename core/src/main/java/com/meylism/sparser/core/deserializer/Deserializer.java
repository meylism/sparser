package com.meylism.sparser.core.deserializer;

/**
 * Sparser expects parsers to implement this interface.
 */
public interface Deserializer {
  Object deserialize(Object data) throws Exception;
}
