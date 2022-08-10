package com.meylism.sparser;

import com.meylism.sparser.predicate.ConjunctiveClause;
import com.meylism.sparser.predicate.ExactMatchPredicate;
import com.meylism.sparser.predicate.PredicateValue;
import com.meylism.sparser.support.FileFormat;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class CalibrationTest {
  private ArrayList<String> records;
  private Sparser sparser = new Sparser.SparserBuilder(FileFormat.JSON).build();

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

    ExactMatchPredicate esmp1 = new ExactMatchPredicate("text", new PredicateValue("elon"));
    ExactMatchPredicate esmp2 = new ExactMatchPredicate("president", new PredicateValue("musk"));
    ExactMatchPredicate esmp3 = new ExactMatchPredicate("lang", new PredicateValue("biden"));

    clause1.add(esmp1);
    clause2.add(esmp2);
    clause3.add(esmp3);

    sparser.compile(Arrays.asList(clause1, clause2, clause3));

    this.records = Utils.loadJson("benchmark/twitter2.json");
  }

}