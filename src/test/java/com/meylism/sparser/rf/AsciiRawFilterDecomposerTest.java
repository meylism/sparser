package com.meylism.sparser.rf;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class AsciiRawFilterDecomposerTest {
  @Test
  public void testTheNumberOfRawFilters() {
    ArrayList<String> strs = new ArrayList<>();
    strs.add("meylis");
    strs.add("matiyev");

    AsciiRawFilter arfs = AsciiRawFilterDecomposer.decompose(strs);

    Assert.assertEquals(arfs.getRawFilters().size(), 9);
  }

  @Test
  public void testTheZeroNumberOfRawFilters() {
    ArrayList<String> predicates = new ArrayList<>();

    AsciiRawFilter arfs = AsciiRawFilterDecomposer.decompose(predicates);

    Assert.assertEquals(arfs.getRawFilters().size(), 0);
  }
}