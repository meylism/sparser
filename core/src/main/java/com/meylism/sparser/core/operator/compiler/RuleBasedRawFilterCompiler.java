package com.meylism.sparser.core.operator.compiler;

import com.meylism.sparser.core.Configuration;
import com.meylism.sparser.core.FileFormat;
import com.meylism.sparser.core.operator.FilterOperator;
import com.meylism.sparser.core.operator.utf.UTF8ExactMatchFilter;
import com.meylism.sparser.core.predicate.Predicate;
import com.meylism.sparser.core.predicate.SimplePredicate;
import com.meylism.sparser.core.support.PredicateSupport;
import com.meylism.sparser.core.support.RawFilterSupport;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Compiles filter operators(primitives) based on the provided support map.
 */
public class RuleBasedRawFilterCompiler extends RawFilterCompiler {
  private ArrayList<String> keyTokens;
  private ArrayList<String> valueTokens;
  private Map<FileFormat, Map<PredicateSupport, List<RawFilterSupport>>> supportMap;

  public RuleBasedRawFilterCompiler(Configuration configuration) {
    super(configuration);
    supportMap = initMappingOfSupport();
  }

  public void compile(Predicate predicate) {
    switch (predicate.getType()) {
    case OR:
    case AND:
      // junction
      for (Predicate p : predicate.getChildren())
        compile(p);
      break;
    default:
      // leaf node
      Set<FilterOperator> filters = processPredicate((SimplePredicate) predicate);
      ((SimplePredicate) predicate).setRawFilters(filters);
    }
  }

  /**
   * Compile filter primitives for the given simple predicate. A simple predicate is a predicate without junctions.
   */
  private Set<FilterOperator> processPredicate(SimplePredicate predicate) {
    keyTokens = this.tokenize(predicate.getKey().getColumnName());
    valueTokens = this.tokenize(predicate.getValue());
    Set<FilterOperator> rawFilters = new HashSet<>(getRawFilter(predicate.getType()));

    return rawFilters;
  }

  private ArrayList<FilterOperator> getRawFilter(PredicateSupport support) {
    ArrayList<FilterOperator> rawFilters = new ArrayList<>();
    List<RawFilterSupport> rawFilterSupportList = supportMap.get(getConfiguration().getFileFormat()).get(support);

    for (RawFilterSupport rawFilterSupport : rawFilterSupportList) {
      switch (rawFilterSupport) {
      case UTF_SUBSTRING_SEARCH:
        rawFilters.addAll(valueTokens.stream().map(UTF8ExactMatchFilter::new).collect(Collectors.toList()));
        break;
      //      case UTF_KEY_VALUE_SEARCH:
      //        rawFilters.addAll(valueTokens.stream().map(UTF8KeyValueMatchFilter::new).collect(Collectors.toList()));
      default:
        throw new RuntimeException(rawFilterSupport + " is not implemented yet");
      }
    }

    return rawFilters;
  }

  private Map<FileFormat, Map<PredicateSupport, List<RawFilterSupport>>> initMappingOfSupport() {
    Map<FileFormat, Map<PredicateSupport, List<RawFilterSupport>>> supportMap = new HashMap<>();

    // JSON
    HashMap<PredicateSupport, List<RawFilterSupport>> jsonPredicateSupport = new HashMap();

    jsonPredicateSupport.put(PredicateSupport.EXACT_MATCH, Arrays.asList(RawFilterSupport.UTF_SUBSTRING_SEARCH));
    jsonPredicateSupport.put(PredicateSupport.KEY_VALUE_MATCH, Arrays.asList(RawFilterSupport.UTF_KEY_VALUE_SEARCH));

    supportMap.put(FileFormat.JSON, jsonPredicateSupport);

    // ORC
    // PARQUET
    return supportMap;
  }

  private ArrayList<String> tokenize(String token) {
    ArrayList<String> substrings = new ArrayList<>();
    substrings.add(token);

    for (int j = 0; j <= token.length() - Configuration.SUBSTRING_SIZE; j++) {
      if (token.length() == Configuration.SUBSTRING_SIZE && j == 0)
        continue;
      substrings.add(token.substring(j, j + Configuration.SUBSTRING_SIZE));
    }
    return substrings;
  }
}
