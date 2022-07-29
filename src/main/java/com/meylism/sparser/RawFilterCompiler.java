package com.meylism.sparser;

import com.meylism.sparser.predicate.ExactMatchPredicate;
import com.meylism.sparser.predicate.SimplePredicate;
import com.meylism.sparser.rf.RawFilter;
import com.meylism.sparser.rf.UTFSubstringSearchRF;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RawFilterCompiler {
  private Configuration conf;
  private ArrayList<String> keyTokens;
  private ArrayList<String> valueTokens;

  public RawFilterCompiler(Configuration configuration) {
    this.conf = configuration;
  }

  public ArrayList<com.meylism.sparser.rf.RawFilter> compile(SimplePredicate predicate) {
    keyTokens = this.tokenize(predicate.getKey());
    valueTokens = this.tokenize(predicate.getValue().getValue());
    ArrayList<RawFilter> rawFilters = new ArrayList<>();
    List<FilterSupport> supportedFilters = this.conf.getFileFormat().getSupportedFilters();

    for (FilterSupport support : supportedFilters) {
      rawFilters.addAll(getRawFilter(support));
    }

    return rawFilters;
  }

  private ArrayList<com.meylism.sparser.rf.RawFilter> getRawFilter(
      FilterSupport support) {
    List<com.meylism.sparser.rf.RawFilter> rawFilters;

    switch (support.getPredicate()){
    case EXACT_STRING_MATCH:
      break;
    case CONTAINS_KEY:
    case CONTAINS_STRING:
    case KEY_VALUE_MATCH:
    default:
      throw new RuntimeException(support.getPredicate().toString() + " is not implemented yet");
    }

    switch (support.getRawFilter()){
    case UTF_SUBSTRING_SEARCH:
      rawFilters = valueTokens.stream().map(UTFSubstringSearchRF::new).collect(Collectors.toList());
      break;
    case UTF_KEY_VALUE_SEARCH:
    default:
      throw new RuntimeException(support.getPredicate().toString() + " is supported but " +
          support.getRawFilter().toString() + "is not supported yet");
    }

    return (ArrayList)rawFilters;
  }

  ArrayList<String> tokenize(String token) {
    ArrayList<String> substrings = new ArrayList<>();

    for(int j=0; j<=token.length()-Configuration.SUBSTRING_SIZE; j++) {
      if (token.length() == Configuration.SUBSTRING_SIZE && j == 0) continue;
      substrings.add(token.substring(j, j+Configuration.SUBSTRING_SIZE));
    }
    return substrings;
  }
}
