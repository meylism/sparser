package com.meylism.sparser;

public class Utils {
  public static long timeStart() {
    return System.nanoTime();
  }

  public static long timeStop(long start) {
    return System.nanoTime() - start;
  }
}
