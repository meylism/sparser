package com.meylism.sparser.benchmark.json.queries;

import com.google.auto.service.AutoService;
import com.meylism.sparser.benchmark.QueryDescription;
import com.meylism.sparser.benchmark.json.DefaultJsonQueryDescription;
import com.meylism.sparser.core.predicate.Predicate;
import com.meylism.sparser.core.predicate.PredicateKey;
import com.meylism.sparser.core.predicate.simple.KeyValueMatchPredicate;

@AutoService(QueryDescription.class)
public class GH2 extends DefaultJsonQueryDescription {
  @Override public String getName() {
    return "gh2";
  }

  @Override public String getQueryDescription() {
    return "WHERE type = 'PushEvent'"; // or any other description with words
  }

  @Override public Predicate getQuery() {
    return new KeyValueMatchPredicate(new PredicateKey("type"), "PushEvent");
  }
}
