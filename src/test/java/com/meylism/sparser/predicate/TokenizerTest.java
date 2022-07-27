package com.meylism.sparser.predicate;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class TokenizerTest {
  @Test
  public void testTheNumberOfTokenizedSubstrings() {
    List<String> substrings = Tokenizer.tokenize("Budapest");
    Assert.assertEquals(5, substrings.size());
  }
}