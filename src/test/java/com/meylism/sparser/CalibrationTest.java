package com.meylism.sparser;

import com.meylism.sparser.benchmark.Utils;
import com.meylism.sparser.predicate.ConjunctiveClause;
import com.meylism.sparser.predicate.ExactStringMatchPredicate;
import com.meylism.sparser.predicate.PredicateValue;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class CalibrationTest {
  private ArrayList<String> records;
  private Sparser sparser = new Sparser();

  @Test
  public void test() throws Exception {
    sparser.calibrate(records);

    assertEquals(true, true);
  }

  @Before
  public void setUp() throws IOException {
    ConjunctiveClause clause1 = new ConjunctiveClause();
    ConjunctiveClause clause2 = new ConjunctiveClause();
    ConjunctiveClause clause3 = new ConjunctiveClause();

    ExactStringMatchPredicate esmp1 = new ExactStringMatchPredicate("text", new PredicateValue("Ronaldo"));
    ExactStringMatchPredicate esmp2 = new ExactStringMatchPredicate("president", new PredicateValue("Biden"));
    ExactStringMatchPredicate esmp3 = new ExactStringMatchPredicate("lang", new PredicateValue("en"));



    clause1.add(esmp1);
    clause2.add(esmp2);
    clause3.add(esmp3);

    ArrayList<ConjunctiveClause> clauses = new ArrayList<>();
    clauses.add(clause1);
    clauses.add(clause2);
    clauses.add(clause3);

    sparser.compile(clauses);

    this.records = Utils.loadJson("twitter.json");
  }

}