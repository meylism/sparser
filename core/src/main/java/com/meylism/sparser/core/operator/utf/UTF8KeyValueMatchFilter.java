package com.meylism.sparser.core.operator.utf;

import com.meylism.sparser.core.operator.KeyValueMatchOperator;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

public class UTF8KeyValueMatchFilter extends KeyValueMatchOperator {

  public UTF8KeyValueMatchFilter(String key, String value, List<Character> delimiters) {
    super(key, value, delimiters);
  }

  @Override public Boolean evaluate(Object record) {
    throw new NotImplementedException();
  }
}
