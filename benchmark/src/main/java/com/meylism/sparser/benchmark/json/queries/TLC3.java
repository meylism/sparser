package com.meylism.sparser.benchmark.json.queries;

import com.google.auto.service.AutoService;
import com.meylism.sparser.benchmark.QueryDescription;
import com.meylism.sparser.benchmark.json.DefaultJsonQueryDescription;
import com.meylism.sparser.core.predicate.Predicate;
import com.meylism.sparser.core.predicate.PredicateKey;
import com.meylism.sparser.core.predicate.SimplePredicate;
import com.meylism.sparser.core.predicate.junction.Or;
import com.meylism.sparser.core.predicate.simple.KeyValueMatchPredicate;

@AutoService(QueryDescription.class)
public class TLC3 extends DefaultJsonQueryDescription {
  @Override public String getName() {
    return "tlc3";
  }

  @Override public String getQueryDescription() {
    return "WHERE VendorID = '1' OR VendorID = '2'";
  }

  @Override public Predicate getQuery() {
    return new KeyValueMatchPredicate(new PredicateKey("payment_type"), "1");
  }
}