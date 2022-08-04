package com.meylism.sparser;

import com.meylism.sparser.predicate.SimplePredicate;
import com.meylism.sparser.rf.RawFilter;
import com.meylism.sparser.rf.UTFSubstringSearchRF;
import com.meylism.sparser.support.FilterSupport;

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

  public ArrayList<RawFilter> compile(SimplePredicate predicate) {
    keyTokens = this.tokenize(predicate.getKey());
    valueTokens = this.tokenize(predicate.getValue().getValue());
    ArrayList<RawFilter> rawFilters = new ArrayList<>();
    List<FilterSupport> supportedFilters = this.conf.getFileFormat().getSupportedFilters();

    for (FilterSupport support : supportedFilters) {
      rawFilters.addAll(getRawFilter(support));
    }

    return rawFilters;
  }

  private ArrayList<RawFilter> getRawFilter(
      FilterSupport support) {
    List<RawFilter> rawFilters;

    switch (support.getPredicateSupport()){
    case EXACT_STRING_MATCH:
      break;
    case CONTAINS_KEY:
    case CONTAINS_STRING:
    case KEY_VALUE_MATCH:
    default:
      throw new RuntimeException(support.getPredicateSupport().toString() + " is not implemented yet");
    }

    switch (support.getRawFilterSupport()){
    case UTF_SUBSTRING_SEARCH:
      rawFilters = valueTokens.stream().map(UTFSubstringSearchRF::new).collect(Collectors.toList());
      break;
    case UTF_KEY_VALUE_SEARCH:
    default:
      throw new RuntimeException(support.getPredicateSupport().toString() + " is supported but " +
          support.getRawFilterSupport().toString() + "is not supported yet");
    }

    return (ArrayList)rawFilters;
  }

  ArrayList<String> tokenize(String token) {
    ArrayList<String> substrings = new ArrayList<>();

    for(int j=0; j<=token.length()-conf.getSubstringSize(); j++) {
      if (token.length() == conf.getSubstringSize() && j == 0) continue;
      substrings.add(token.substring(j, j+conf.getSubstringSize()));
    }
    return substrings;
  }
}
