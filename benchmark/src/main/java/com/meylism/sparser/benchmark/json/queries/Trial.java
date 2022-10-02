package com.meylism.sparser.benchmark.json.queries;

import com.google.auto.service.AutoService;
import com.meylism.sparser.benchmark.Dataset;
import com.meylism.sparser.benchmark.QueryDescription;
import com.meylism.sparser.benchmark.json.DefaultJsonQueryDescription;
import com.meylism.sparser.core.predicate.Predicate;
import com.meylism.sparser.core.predicate.PredicateKey;
import com.meylism.sparser.core.predicate.SimplePredicate;
import com.meylism.sparser.core.predicate.junction.And;
import com.meylism.sparser.core.predicate.junction.Or;
import com.meylism.sparser.core.predicate.simple.ExactMatchPredicate;

@AutoService(QueryDescription.class)
public class Trial extends DefaultJsonQueryDescription {
  @Override public String getName() {
    return "trial";
  }

  @Override public String getQueryAsString() {
    return "trial";
  }

  @Override public Predicate getQuery() {
    And<SimplePredicate> clause1 = new And<>();
    And<SimplePredicate> clause2 = new And<>();
    And<SimplePredicate> clause3 = new And<>();

    ExactMatchPredicate esmp1 = new ExactMatchPredicate(new PredicateKey("text"), "musk");
    ExactMatchPredicate esmp2 = new ExactMatchPredicate(new PredicateKey("text"), "elon");
    ExactMatchPredicate esmp3 = new ExactMatchPredicate(new PredicateKey("text"), "biden");

    clause1.add(esmp1);
    clause2.add(esmp2);
    clause3.add(esmp3);

    Or<And<SimplePredicate>> predicate = new Or<>();
    predicate.add(clause1);
    predicate.add(clause2);
    predicate.add(clause3);

    return predicate;
  }

  @Override public Dataset getDataset() {
    return Dataset.GHARCHIVE;
  }
}
