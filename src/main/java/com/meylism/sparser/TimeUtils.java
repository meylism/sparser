package com.meylism.sparser;

class TimeUtils {
  public static long timeStart() {
    return System.nanoTime();
  }

  public static long timeStop(long start) {
    return System.nanoTime() - start;
  }
}
