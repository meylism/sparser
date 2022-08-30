package com.meylism.sparser.rf.compiler;

import com.meylism.sparser.Configuration;
import com.meylism.sparser.FileFormat;
import com.meylism.sparser.predicate.ConjunctiveClause;
import com.meylism.sparser.predicate.SimplePredicate;
import com.meylism.sparser.rf.RawFilter;
import com.meylism.sparser.rf.UTFSubstringSearchRF;
import com.meylism.sparser.support.PredicateSupport;
import com.meylism.sparser.support.RawFilterSupport;

import java.util.*;
import java.util.stream.Collectors;

public class RuleBasedRawFilterCompiler implements RawFilterCompiler {
  private Configuration conf;
  private ArrayList<String> keyTokens;
  private ArrayList<String> valueTokens;
  private Map<FileFormat, Map<PredicateSupport, List<RawFilterSupport>>> supportMap;

  public RuleBasedRawFilterCompiler(Configuration configuration) {
    this.conf = configuration;
    initMappingOfSupport();
  }

  public void compile() {
    for (ConjunctiveClause clause : this.conf.getClauses()) {
      for (SimplePredicate predicate : clause.getSimplePredicates()) {
        predicate.setRawFilters(processPredicate(predicate));
      }
    }
  }

  private ArrayList<RawFilter> processPredicate(SimplePredicate predicate) {
    keyTokens = this.tokenize(predicate.getKey());
    valueTokens = this.tokenize(predicate.getValue().getValue());
    ArrayList<RawFilter> rawFilters = new ArrayList<>();

    rawFilters.addAll(getRawFilter(predicate.getType()));

    return rawFilters;
  }

  private ArrayList<RawFilter> getRawFilter(PredicateSupport support) {
    List<RawFilter> rawFilters = new ArrayList<>();
    List<RawFilterSupport> rawFilterSupportList = supportMap.get(conf.getFileFormat()).get(support);

    for (RawFilterSupport rawFilterSupport : rawFilterSupportList) {
      switch (rawFilterSupport) {
      case UTF_SUBSTRING_SEARCH:
        rawFilters.addAll(valueTokens.stream().map(UTFSubstringSearchRF::new).collect(Collectors.toList()));
        break;
      default:
        throw new RuntimeException(rawFilterSupport + " is not implemented yet");
      }
    }

    return (ArrayList<RawFilter>) rawFilters;
  }

  private ArrayList<String> tokenize(String token) {
    ArrayList<String> substrings = new ArrayList<>();
    substrings.add(token);

    for (int j = 0; j <= token.length() - Configuration.SUBSTRING_SIZE; j++) {
      if (token.length() == Configuration.SUBSTRING_SIZE && j == 0)
        continue;
      substrings.add(token.substring(j, Configuration.SUBSTRING_SIZE));
    }
    return substrings;
  }

  private void initMappingOfSupport() {
    supportMap = new HashMap<>();

    // JSON
    HashMap<PredicateSupport, List<RawFilterSupport>> jsonPredicateSupport = new HashMap();
    jsonPredicateSupport.put(PredicateSupport.EXACT_STRING_MATCH,
        Arrays.asList(RawFilterSupport.UTF_SUBSTRING_SEARCH));

    supportMap.put(FileFormat.JSON, jsonPredicateSupport);

    // ORC
    // PARQUET
  }
}
