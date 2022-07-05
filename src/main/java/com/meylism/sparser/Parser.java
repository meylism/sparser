package com.meylism.sparser;

public interface Parser {
  Object deserialize(String record) throws Exception;
}
