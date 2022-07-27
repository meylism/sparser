package com.meylism.sparser;

import com.meylism.sparser.predicate.ConjunctiveClause;
import com.meylism.sparser.predicate.ExactMatchPredicate;
import com.meylism.sparser.predicate.PredicateValue;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class CalibrationTest {
  private ArrayList<String> records;
  private Sparser sparser = new Sparser.SparserBuilder().build();

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

    ExactMatchPredicate esmp1 = new ExactMatchPredicate("text", new PredicateValue("Ronaldo"));
    ExactMatchPredicate esmp2 = new ExactMatchPredicate("president", new PredicateValue("Biden"));
    ExactMatchPredicate esmp3 = new ExactMatchPredicate("lang", new PredicateValue("en"));



    clause1.add(esmp1);
    clause2.add(esmp2);
    clause3.add(esmp3);

    ArrayList<ConjunctiveClause> clauses = new ArrayList<>();
    clauses.add(clause1);
    clauses.add(clause2);
    clauses.add(clause3);

    sparser.compile(clauses);

    this.records = Utils.loadJson("benchmark/twitter2.json");
  }

}