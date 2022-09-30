package com.meylism.sparser.optimization.transformation;

import com.meylism.sparser.Configuration;
import com.meylism.sparser.operator.FilterOperator;
import com.meylism.sparser.predicate.Predicate;
import com.meylism.sparser.predicate.SimplePredicate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Search space for choosing the best cascade is combinatorial. This transformer tries to restrict the number of
 * filters considered during the search to avoid long search times.
 *
 * If the total number of filters is greater than {@link Configuration#MAX_RF MAX_RF}, this transformer select only a
 * certain number of filter from each simple predicate.
 */
public class FilterPruningTransformer extends Transformer {

  public FilterPruningTransformer(Configuration configuration) {
    super(configuration);
  }

  @Override public void transform(Predicate predicate) {
    if (getTotalNumberOfFilters(predicate) > Configuration.MAX_RF) {
      Integer filterPerSimplePredicate = Math.round(Configuration.MAX_RF/getTotalNumberOfSimplePredicates(predicate));
      prune(predicate, filterPerSimplePredicate);
    }
  }

  private void prune(Predicate predicate, Integer filterPerSimplePredicate) {
    switch (predicate.getType()) {
    case OR:
    case AND:
      // junction
      for (Predicate p : predicate.getChildren())
        prune(p, filterPerSimplePredicate);
      break;
    default:
      // leaf node

      // this is an attempt to randomly select filterPerSimplePredicate number of filters from each simple predicate

      // random selection is achieved through shuffling: shuffle the collection and choose the first
      // filterPerSimplePredicate number of filters
      SimplePredicate asSimplePredicate = (SimplePredicate) predicate;
      if (asSimplePredicate.getRawFilters().size() < filterPerSimplePredicate) return;
      List<FilterOperator> filtersAsList = new ArrayList<>(asSimplePredicate.getRawFilters());
      Collections.shuffle(filtersAsList);
      HashSet<FilterOperator> newFilterSet = new HashSet<>();
      for (int i=0; i<filterPerSimplePredicate; i++)
        newFilterSet.add(filtersAsList.get(i));
      asSimplePredicate.setRawFilters(newFilterSet);
    }
  }

  /**
   * Get the total number of filters in the given predicate.
   */
  private int getTotalNumberOfFilters(Predicate predicate) {
    switch (predicate.getType()) {
    case OR:
    case AND:
      // junction
      int subSum = 0;
      for (Predicate p : predicate.getChildren())
        subSum += getTotalNumberOfFilters(p);
      return subSum;
    default:
      // leaf node
      return ((SimplePredicate)predicate).getRawFilters().size();
    }
  }

  /**
   * Get the total number of simple predicates in the given predicate.
   * A simple predicate is any predicate except junction.
   */
  private int getTotalNumberOfSimplePredicates(Predicate predicate) {
    switch (predicate.getType()) {
    case OR:
    case AND:
      // junction
      int subSum = 0;
      for (Predicate p : predicate.getChildren())
        subSum += getTotalNumberOfSimplePredicates(p);
      return subSum;
    default:
      // leaf node
      return 1;
    }
  }
}
