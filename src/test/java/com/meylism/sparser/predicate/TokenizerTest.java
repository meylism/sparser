package com.meylism.sparser.predicate;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class TokenizerTest {
  @Test
  public void testTheNumberOfTokenizedSubstrings() {
    ArrayList<String> substrings = Tokenizer.tokenize("Budapest");
    Assert.assertEquals(5, substrings.size());
  }
}