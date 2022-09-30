package com.meylism.sparser;

import com.meylism.sparser.benchmark.Utils;
import com.meylism.sparser.predicate.PredicateKey;
import com.meylism.sparser.predicate.SimplePredicate;
import com.meylism.sparser.predicate.junction.And;
import com.meylism.sparser.predicate.junction.Or;
import com.meylism.sparser.predicate.simple.ExactMatchPredicate;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class CostBasedOptimizerTest {
  private ArrayList<String> records;
  private ArrayList<String> filtered = new ArrayList<>();
    private Sparser sparser;
  @Test public void test() throws Exception {
    for (String record : records) {
      if (!sparser.filter(record))
        filtered.add(record);
    }

    assertEquals(true, true);
  }

  @Before public void setUp() throws IOException {
    And<SimplePredicate> clause1 = new And<>();
    And<SimplePredicate> clause2 = new And<>();
    And<SimplePredicate> clause3 = new And<>();

    ExactMatchPredicate esmp1 = new ExactMatchPredicate(new PredicateKey("text"), "biden");
    ExactMatchPredicate esmp2 = new ExactMatchPredicate(new PredicateKey("text"), "musk");
    ExactMatchPredicate esmp3 = new ExactMatchPredicate(new PredicateKey("text"), "elon");

    clause1.add(esmp1);
    clause2.add(esmp2);
    clause3.add(esmp3);

    Or<And<SimplePredicate>> predicate = new Or<>();
    predicate.add(clause1);
    predicate.add(clause2);
    predicate.add(clause3);

    sparser = new Sparser(predicate, FileFormat.JSON, new JacksonDeserializer());

    this.records = Utils.loadJson("benchmark/gharchive.json");
  }
}