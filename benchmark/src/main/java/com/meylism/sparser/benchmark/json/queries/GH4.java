package com.meylism.sparser.benchmark.json.queries;

import com.google.auto.service.AutoService;
import com.meylism.sparser.benchmark.QueryDescription;
import com.meylism.sparser.benchmark.json.DefaultJsonQueryDescription;
import com.meylism.sparser.core.predicate.Predicate;
import com.meylism.sparser.core.predicate.PredicateKey;
import com.meylism.sparser.core.predicate.SimplePredicate;
import com.meylism.sparser.core.predicate.junction.And;
import com.meylism.sparser.core.predicate.junction.Or;
import com.meylism.sparser.core.predicate.simple.ExactMatchPredicate;
import com.meylism.sparser.core.predicate.simple.KeyValueMatchPredicate;

@AutoService(QueryDescription.class)
public class GH4 extends DefaultJsonQueryDescription {

  @Override public String getName() {
    return "gh4";
  }

  @Override public String getQueryDescription() {
    return "WHERE body type = 'PushEvent' OR type = 'WatchEvent'";
  }

  @Override public Predicate getQuery() {
    Or<SimplePredicate> or = new Or<>();

    SimplePredicate event1 = new KeyValueMatchPredicate(new PredicateKey("type"), "PushEvent");
    SimplePredicate event2 = new KeyValueMatchPredicate(new PredicateKey("type"), "WatchEvent");

    or.add(event1);
    or.add(event2);

    return or;
  }
}