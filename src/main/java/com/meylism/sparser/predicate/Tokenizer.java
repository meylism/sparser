package com.meylism.sparser.predicate;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {
  final static Integer SUBSTRING_SIZE = 4;

  static List<String> tokenize(String token) {
    ArrayList<String> substrings = new ArrayList<>();

    for(int j=0; j<=token.length()-SUBSTRING_SIZE; j++) {
      if (token.length() == SUBSTRING_SIZE && j == 0) continue;
      substrings.add(token.substring(j, j+SUBSTRING_SIZE));
    }
    return substrings;
  }
}
