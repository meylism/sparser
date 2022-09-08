package com.meylism.sparser;

import com.meylism.sparser.benchmark.Utils;
import com.meylism.sparser.predicate.ConjunctiveClause;
import com.meylism.sparser.predicate.ExactMatchPredicate;
import com.meylism.sparser.predicate.PredicateKey;
import com.meylism.sparser.predicate.PredicateValue;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class CostBasedCalibratorTest {
  private ArrayList<String> records;
    private Sparser sparser = new Sparser(FileFormat.JSON);

  @Test public void test() throws Exception {
    sparser.calibrate(records, new JacksonDeserializer());

    assertEquals(true, true);
  }

  @Before public void setUp() throws IOException {
    ConjunctiveClause clause1 = new ConjunctiveClause();
    ConjunctiveClause clause2 = new ConjunctiveClause();
    ConjunctiveClause clause3 = new ConjunctiveClause();

    ExactMatchPredicate esmp1 = new ExactMatchPredicate(new PredicateKey("text"), new PredicateValue("elon"));
    ExactMatchPredicate esmp2 = new ExactMatchPredicate(new PredicateKey("text"), new PredicateValue("musk"));
    ExactMatchPredicate esmp3 = new ExactMatchPredicate(new PredicateKey("text"), new PredicateValue("biden"));

    clause1.add(esmp1);
    clause2.add(esmp2);
    clause3.add(esmp3);

    sparser.compile(Arrays.asList(clause1, clause2, clause3));

    this.records = Utils.loadJson("benchmark/twitter2.json");
  }

}