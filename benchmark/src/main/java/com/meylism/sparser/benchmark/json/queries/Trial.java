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


// This is annotation necessary to be able to discover queries at run-time.
@AutoService(QueryDescription.class)
public class Trial extends DefaultJsonQueryDescription {
  @Override public String getName() {
    return "trial";
  }

  @Override public String getQueryDescription() {
    return "foobar";
  }

  @Override public Predicate getQuery() {
    Or<And> or = new Or<>();
    And<SimplePredicate> and1 = new And<>();
    And<SimplePredicate> and2 = new And<>();
    And<SimplePredicate> and3 = new And<>();
    And<SimplePredicate> and4 = new And<>();

    SimplePredicate org = new KeyValueMatchPredicate(new PredicateKey("organization"), "apache");

    SimplePredicate vendor1 = new KeyValueMatchPredicate(new PredicateKey("type"), "PushEvent");
    SimplePredicate vendor2 = new KeyValueMatchPredicate(new PredicateKey("type"), "WatchEvent");
    SimplePredicate vendor3 = new KeyValueMatchPredicate(new PredicateKey("type"), "CreateEvent");
    SimplePredicate vendor4 = new KeyValueMatchPredicate(new PredicateKey("type"), "WatchEvent");

    and1.add(org);
    and1.add(vendor1);

    and2.add(org);
    and2.add(vendor2);

    and3.add(org);
    and3.add(vendor3);

    and4.add(org);
    and4.add(vendor4);

    or.add(and1);
    or.add(and2);
    or.add(and3);
    or.add(and4);

    return new KeyValueMatchPredicate(new PredicateKey("VendorID"), "2");
  }
}
