package com.meylism.sparser.benchmark.json.queries;

import com.google.auto.service.AutoService;
import com.meylism.sparser.benchmark.QueryDescription;
import com.meylism.sparser.benchmark.json.DefaultJsonQueryDescription;
import com.meylism.sparser.core.predicate.Predicate;
import com.meylism.sparser.core.predicate.PredicateKey;
import com.meylism.sparser.core.predicate.SimplePredicate;
import com.meylism.sparser.core.predicate.junction.And;
import com.meylism.sparser.core.predicate.junction.Or;
import com.meylism.sparser.core.predicate.simple.KeyValueMatchPredicate;

@AutoService(QueryDescription.class)
public class TLC2 extends DefaultJsonQueryDescription {
  @Override public String getName() {
    return "tlc2";
  }

  @Override public String getQueryDescription() {
    return "WHERE VendorID = '1' OR VendorID = '2'";
  }

  @Override public Predicate getQuery() {
    Or<Predicate> or = new Or<>();
    And<SimplePredicate> and1 = new And<>();
    And<SimplePredicate> and2 = new And<>();

    SimplePredicate vendor1 = new KeyValueMatchPredicate(new PredicateKey("VendorID"), "1");
    SimplePredicate vendor2 = new KeyValueMatchPredicate(new PredicateKey("VendorID"), "2");
    SimplePredicate tip = new KeyValueMatchPredicate(new PredicateKey("tip_amount"), "0.0");

    and1.add(vendor1);
    and1.add(tip);

    and2.add(vendor2);
    and2.add(tip);

    or.add(and1);
    or.add(and2);

    return or;
  }
}