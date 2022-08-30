package com.meylism.sparser;

import com.meylism.sparser.calibration.Calibrator;
import com.meylism.sparser.calibration.CostBasedCalibrator;
import com.meylism.sparser.deserializer.Deserializer;
import com.meylism.sparser.filter.Filter;
import com.meylism.sparser.predicate.ConjunctiveClause;
import com.meylism.sparser.rf.RawFilter;
import com.meylism.sparser.rf.compiler.RawFilterCompiler;
import com.meylism.sparser.rf.compiler.RuleBasedRawFilterCompiler;

import java.io.Serializable;
import java.util.List;

/**
 * A facade for Sparser.
 *
 * @author Meylis Matiyev
 */
public class Sparser implements Serializable {
  private final Configuration configuration;
  private List<RawFilter> bestCascade;

  public Sparser(FileFormat fileFormat) {
    configuration = new Configuration();
    configuration.setFileFormat(fileFormat);
    configuration.setFilter(new Filter(configuration, bestCascade));
  }

  /**
   * Compile possible filtering primitives, called raw filters(RFs), from the given query predicate.
   * The function expects the query predicate to be in disjunctive normal form(i.e., of the form (a AND b) OR (c AND
   * …) OR … ). An expression with only conjunctions(AND) is referred to as a conjunctive clause.
   *
   * @param clauses query predicate in DNF, that is, a list of conjunctive clauses
   */
  public void compile(final List<ConjunctiveClause> clauses) {
    configuration.setClauses(clauses);
    RawFilterCompiler filterCompiler = new RuleBasedRawFilterCompiler(this.configuration);
    configuration.setRawFilterCompiler(filterCompiler);

    configuration.getRawFilterCompiler().compile();
  }

  /**
   *
   * @param samples a list of conjunctive clauses
   */
  public void calibrate(List<String> samples, Deserializer deserializer) throws Exception {
    Calibrator calibrator = new CostBasedCalibrator(configuration);
    configuration.setDeserializer(deserializer);
    configuration.setCalibrator(calibrator);

    bestCascade = configuration.getCalibrator().calibrate(samples);
  }

  public Boolean filter(Object record) {
    return configuration.getFilter().filter(record);
  }
}
