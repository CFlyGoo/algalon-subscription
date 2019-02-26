package com.apehat.algalon.subscription.support.descriptor;

import com.apehat.algalon.subscription.SubscriptionDescriptor;
import java.time.Instant;

/**
 * @author cflygoo
 */
public class SimpleSubscriptionDescriptor implements SubscriptionDescriptor {


  private final Instant startTime;
  private final boolean available;

  public SimpleSubscriptionDescriptor(Instant startTime, boolean available) {
    this.startTime = startTime;
    this.available = available;
  }

  @Override
  public boolean isAvailable() {
    return available;
  }

  @Override
  public Instant startTime() {
    return startTime;
  }

  @Override
  public Instant endTime() {
    return Instant.MAX;
  }

  public SubscriptionDescriptor end(Instant endTime) {
    return new RecordableSubscriptionDescriptor(this, endTime);
  }

  @Override
  public String toString() {
    return startTime() + " <<-- available: " + isAvailable() + " -->> " + endTime();
  }
}
