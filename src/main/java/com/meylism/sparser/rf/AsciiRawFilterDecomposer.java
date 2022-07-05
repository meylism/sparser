package com.meylism.sparser.rf;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
  public static AsciiRawFilter decompose(List<String> predicates) {
    AsciiRawFilter arf = new AsciiRawFilter();

    for (int i=0; i<predicates.size(); i++) {
      logger.debug("Decomposing the predicate: " + predicates.get(i));
      arf.getRawFilters().add(predicates.get(i));
      arf.getSources().add(i);
      logger.debug("Ascii raw filter: " + predicates.get(i));

      for(int j=0; j<=predicates.get(i).length()-REG_SIZE; j++) {
        if (predicates.get(i).length() == REG_SIZE && j == 0) continue;

        arf.getRawFilters().add(predicates.get(i).substring(j, j+REG_SIZE));
        arf.getSources().add(i);
        logger.debug("Ascii raw filter: " + arf.getRawFilters().get(arf.getRawFilters().size()-1));
      }
    }

    return arf;
  }
}
