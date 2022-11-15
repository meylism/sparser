package com.meylism.sparser.benchmark.json.queries;

import com.google.auto.service.AutoService;
import com.meylism.sparser.benchmark.QueryDescription;
import com.meylism.sparser.benchmark.json.DefaultJsonQueryDescription;
import com.meylism.sparser.core.predicate.Predicate;
import com.meylism.sparser.core.predicate.PredicateKey;
import com.meylism.sparser.core.predicate.simple.ExactMatchPredicate;

@AutoService(QueryDescription.class)
public class GH5 extends DefaultJsonQueryDescription {

  @Override public String getName() {
    return "gh5";
  }

  @Override public String getQueryDescription() {
    return "WHERE body CONTAINS 'github'";
  }

  @Override public Predicate getQuery() {
    return new ExactMatchPredicate(new PredicateKey("created_at"), "github");
  }
}