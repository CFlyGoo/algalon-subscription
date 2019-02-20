package com.apehat.algalon.subscription;

import java.time.Instant;

/**
 * @author cflygoo
 */
public interface SubscriptionDescriptor {

  boolean isAvailable();

  Instant startTime();

  Instant endTime();

  default boolean contains(Instant instant) {
    return !startTime().isAfter(instant) && endTime().isAfter(instant);
  }
}
