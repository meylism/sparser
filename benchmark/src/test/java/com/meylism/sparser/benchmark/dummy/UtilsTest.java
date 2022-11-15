package com.meylism.sparser.benchmark.dummy;

import com.meylism.sparser.core.operator.utf.UTF8KeyValueMatchFilter;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class UtilsTest {

  @Test
  public void test() throws IOException {
    UTF8KeyValueMatchFilter ky = new UTF8KeyValueMatchFilter("DOLocationID", "2", null);
    UTF8KeyValueMatchFilter ky2 = new UTF8KeyValueMatchFilter("PULocationID", "2", null);
    List<String> res = new ArrayList<>();
    for (String record : Utils.loadJson("/Users/meylismatiyev/Downloads/data-tlcdata/tlcdata.json")) {
      if (ky.evaluate(record) || ky2.evaluate(record))
        res.add(record);
    }
    assertNotNull(res);
  }
}