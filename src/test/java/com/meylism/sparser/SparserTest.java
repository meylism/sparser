//package com.meylism.sparser;
//
//import com.meylism.sparser.predicate.ConjunctiveClause;
//import com.meylism.sparser.predicate.ExactMatchPredicate;
//import com.meylism.sparser.predicate.PredicateValue;
//import org.junit.Assert;
//import org.junit.Test;
//
//import java.io.IOException;
//import java.util.ArrayList;
//
//public class SparserTest {
//  @Test public void testCompile() throws IOException {
//    ExactMatchPredicate exactStringMatchPredicate = new ExactMatchPredicate("city", new PredicateValue("Budapest"));
//
//    ConjunctiveClause cc = new ConjunctiveClause();
//    cc.add(exactStringMatchPredicate);
//
//    ArrayList<ConjunctiveClause> clauses = new ArrayList<>();
//    clauses.add(cc);
//
//    Sparser sparser = new Sparser(FileFormat.JSON);
//    sparser.compile(clauses);
//
//    // serde
//    //    FileOutputStream fileOutputStream
//    //        = new FileOutputStream("yourfile.txt");
//    //    ObjectOutputStream objectOutputStream
//    //        = new ObjectOutputStream(fileOutputStream);
//    //    objectOutputStream.writeObject(sparser);
//    //    objectOutputStream.flush();
//    //    objectOutputStream.close();
//
//    Assert.assertEquals(true, true);
//  }
//}