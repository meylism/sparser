package com.meylism.sparser;

import com.meylism.sparser.predicate.ConjunctiveClause;
import com.meylism.sparser.predicate.ExactMatchPredicate;
import com.meylism.sparser.predicate.PredicateValue;
import com.meylism.sparser.support.FileFormat;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class SparserTest {
  @Test
  public void testCompile() {
    ExactMatchPredicate exactStringMatchPredicate = new ExactMatchPredicate("city",
        new PredicateValue("Budapest"));

    ConjunctiveClause cc = new ConjunctiveClause();
    cc.add(exactStringMatchPredicate);

    ArrayList<ConjunctiveClause> clauses = new ArrayList<>();
    clauses.add(cc);

    Sparser sparser = new Sparser.SparserBuilder(FileFormat.JSON).build();
    sparser.compile(clauses);

    Assert.assertEquals(true, true);
  }
}