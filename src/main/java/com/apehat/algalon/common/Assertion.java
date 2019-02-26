package com.apehat.algalon.common;

import java.time.Instant;

/**
 * @author cflygoo
 */
public final class Assertion {

  private Assertion() {
  }

  public static void notLaterThanCalling(Instant time) {
    Instant now = Instant.now();
    if (time.isAfter(now)) {
      throw new IllegalStateException("The timestamp cannot be latter than the " + now);
    }
  }
}
