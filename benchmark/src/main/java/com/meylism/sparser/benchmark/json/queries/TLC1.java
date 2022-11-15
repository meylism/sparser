package com.meylism.sparser.benchmark.json.queries;

import com.google.auto.service.AutoService;
import com.meylism.sparser.benchmark.QueryDescription;
import com.meylism.sparser.benchmark.json.DefaultJsonQueryDescription;
import com.meylism.sparser.core.predicate.Predicate;
import com.meylism.sparser.core.predicate.PredicateKey;
import com.meylism.sparser.core.predicate.SimplePredicate;
import com.meylism.sparser.core.predicate.junction.Or;
import com.meylism.sparser.core.predicate.simple.KeyValueMatchPredicate;


// This is annotation necessary to be able to discover queries at run-time.
@AutoService(QueryDescription.class)
public class TLC1 extends DefaultJsonQueryDescription {
  @Override public String getName() {
    return "tlc1";
  }

  @Override public String getQueryDescription() {
    return "WHERE tip_amount = 0.0";
  }

  @Override public Predicate getQuery() {
    return new KeyValueMatchPredicate(new PredicateKey("tip_amount"), "0.0");
  }
}
