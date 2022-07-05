package com.meylism.sparser;

import com.meylism.sparser.calibration.Calibration;
import com.meylism.sparser.parser.Jackson;
import com.meylism.sparser.rf.AsciiRawFilter;
import com.meylism.sparser.rf.AsciiRawFilterDecomposer;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Sparser {
  private AsciiRawFilter asciiRawFilter;
  private Query query;

  public Sparser(ArrayList<String> predicates) {
    asciiRawFilter = AsciiRawFilterDecomposer.decompose(predicates);
  }

  public void calibrate(List<String> records) throws Exception {
    query = new Calibration().calibrate(records, asciiRawFilter, new Jackson());
  }

  public boolean filter(String record) {
    int passed = 0;
    for (int i=0; i<query.getRawFilters().size(); i++) {
      if (record.indexOf(query.getRawFilters().get(i)) > -1)
        passed++;
    }

    return passed == query.getRawFilters().size();
  }
}
