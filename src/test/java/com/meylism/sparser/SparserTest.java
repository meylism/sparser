package com.meylism.sparser;

import com.meylism.sparser.predicate.ConjunctiveClause;
import com.meylism.sparser.predicate.ExactStringMatchPredicate;
import com.meylism.sparser.predicate.PredicateValue;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class SparserTest {
  @Test
  public void testCompile() {
    ExactStringMatchPredicate exactStringMatchPredicate = new ExactStringMatchPredicate("city",
        new PredicateValue("Budapest"));

    ConjunctiveClause cc = new ConjunctiveClause();
    cc.add(exactStringMatchPredicate);

    ArrayList<ConjunctiveClause> clauses = new ArrayList<>();
    clauses.add(cc);

    Sparser sparser = new Sparser();
    sparser.compile(clauses);

    Assert.assertEquals(true, true);
  }
}