package com.ecommerce.utils;

public class PerformanceTimer {
  private long startTime;

  public void start() {
    startTime = System.nanoTime();
  }

  public long end() {
    return System.nanoTime() - startTime;
  }

  public static String formatDuration(long durationNano) {
    return String.format("%.3f ms", durationNano / 1_000_000.0);
  }
}
