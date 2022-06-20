package com.meylism.sparser.rf;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Decomposer for simple predicates, such as equality and LIKE predicates.
 */
public class AsciiRawFilterDecomposer {
  private static Logger logger = LogManager.getLogger(AsciiRawFilterDecomposer.class);

  static final Integer REG_SIZE = 4;

  /**
   * Decompose simple predicates into raw filters, that is, tokens of size REG_SIZE.
   * @param predicates a list of predicates
   * @return ascii raw filters
   */
  public static ArrayList<AsciiRawFilter> decompose(List<String> predicates) {
    ArrayList<AsciiRawFilter> rawFilters = new ArrayList<>();

    for (int i=0; i<predicates.size(); i++) {
      logger.debug("Decomposing the predicate: " + predicates.get(i));
      rawFilters.add(new AsciiRawFilter(predicates.get(i)));
      logger.debug("Ascii raw filter: " + predicates.get(i));

      for(int j=0; j<=predicates.get(i).length()-REG_SIZE; j++) {
        if (predicates.get(i).length() == REG_SIZE && j == 0) continue;

        rawFilters.add(new AsciiRawFilter(predicates.get(i).substring(j, j+REG_SIZE)));
        logger.debug("Ascii raw filter: " + rawFilters.get(rawFilters.size()-1));
      }
    }

    return rawFilters;
  }
}
