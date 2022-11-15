package com.meylism.sparser.core.operator.utf;

import com.meylism.sparser.core.operator.KeyValueMatchFilterOperator;
import java.util.List;

public class UTF8KeyValueMatchFilter extends KeyValueMatchFilterOperator {

  public UTF8KeyValueMatchFilter(String key, String value, List<Character> delimiters) {
    super(key, value, delimiters);
  }

  @Override public Boolean evaluate(Object record) {
    String aString = (String) record;
    int res = aString.indexOf(getKey());
    boolean result = (res > -1);
    if (result) {
      res += getKey().length();
      while(true) {
        if (aString.charAt(res) == ' ' || aString.charAt(res) == ':' || aString.charAt(res) == '"') {
          res += 1;
        } else {
           if(aString.startsWith(getValue(), res)){
             res += getValue().length();
             while(true) {
               if (aString.charAt(res) == ' ' || aString.charAt(res) == ',' || aString.charAt(res) == '"')
                 return true;
               return false;
             }
           } else {
             return false;
           }
        }
      }
    }
    return false;
  }
}
