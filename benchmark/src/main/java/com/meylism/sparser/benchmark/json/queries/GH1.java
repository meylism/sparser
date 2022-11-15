package com.meylism.sparser.benchmark.json.queries;

import com.google.auto.service.AutoService;
import com.meylism.sparser.benchmark.QueryDescription;
import com.meylism.sparser.benchmark.json.DefaultJsonQueryDescription;
import com.meylism.sparser.core.predicate.Predicate;
import com.meylism.sparser.core.predicate.PredicateKey;
import com.meylism.sparser.core.predicate.simple.KeyValueMatchPredicate;

// This annotation is necessary to be able to discover queries at run-time.
@AutoService(QueryDescription.class)
public class GH1 extends DefaultJsonQueryDescription {
  // Queries are identified by their names and are therefore expected to be unique across the project.
  // You will get warned if queries share a name together.
  @Override public String getName() {
    return "gh1";
  }

  @Override public String getQueryDescription() {
    return "WHERE org = 'apache'"; // or any other description with words
  }

  @Override public Predicate getQuery() {
    return new KeyValueMatchPredicate(new PredicateKey("org"), "apache");
  }
}
