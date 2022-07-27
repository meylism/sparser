package com.meylism.sparser;

import com.meylism.sparser.deserializer.Deserializer;
import com.meylism.sparser.deserializer.JacksonDeserializer;
import com.meylism.sparser.predicate.ConjunctiveClause;
import com.meylism.sparser.predicate.SimplePredicate;
import com.meylism.sparser.rf.RawFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * Sparser is a raw filtering solution for filtering records before parsing.
 *
 * @author Meylis Matiyev
 */
public class Sparser {
  private List<ConjunctiveClause> clauses;
  private Calibration calibration;

  private Context context;

  private Sparser(Context context) {
    this.context = context;
  }

  /**
   * Compile possible filtering primitives, called raw filters(RFs), from the given query predicate.
   * The function expects the query predicate to be in disjunctive normal form(i.e., of the form (a AND b) OR (c AND
   * …) OR … ). An expression with only conjunctions(AND) is referred to as a conjunctive clause.
   *
   * @param clauses query predicate in DNF, that is, a list of conjunctive clauses
   */
  public void compile(final List<ConjunctiveClause> clauses) {
    this.clauses = clauses;

    for (ConjunctiveClause clause : clauses) {
      for (SimplePredicate predicate : clause.getSimplePredicates()) {
        predicate.compileRawFilters();
      }
    }
  }

  public void calibrate(List<String> samples, Deserializer deserializer) throws Exception {
    this.calibration = new Calibration(clauses, deserializer);
    this.calibration.calibrate(samples);
  }

  public void calibrate(List<String> samples) throws Exception {
    this.calibrate(samples, new JacksonDeserializer());
  }

  public Boolean filter(String record) {
//    int passed = 0;
    for(RawFilter rawFilter : this.calibration.getBestCascade()) {
      if (rawFilter.evaluate(record))
        return true;
    }
    return false;
  }

  public static class SparserBuilder {
    private Context context = new Context();

    // defaults
    public SparserBuilder() {
      this.context.setFileFormat(FileFormat.JSON);
    }

    public SparserBuilder fileFormat(FileFormat fileFormat) {
      this.context.setFileFormat(fileFormat);
      return this;
    }

    public Sparser build() {
      return new Sparser(this.context);
    }
  }
}
