package com.meylism.sparser.rf;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class AsciiRawFilterDecomposerTest {
  @Test
  public void testTheNumberOfRawFilters() {
    ArrayList<String> predicates = new ArrayList<>();
    predicates.add("meylis");
    predicates.add("matiyev");

    ArrayList<AsciiRawFilter> asciiRawFilters = AsciiRawFilterDecomposer.decompose(predicates);

    Assert.assertEquals(asciiRawFilters.size(), 9);
  }

  @Test
  public void testTheZeroNumberOfRawFilters() {
    ArrayList<String> predicates = new ArrayList<>();

    ArrayList<AsciiRawFilter> asciiRawFilters = AsciiRawFilterDecomposer.decompose(predicates);

    Assert.assertEquals(asciiRawFilters.size(), 0);


  }
}