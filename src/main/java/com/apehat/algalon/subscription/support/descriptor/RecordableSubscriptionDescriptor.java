package com.apehat.algalon.subscription.support.descriptor;

import com.apehat.algalon.subscription.SubscriptionDescriptor;
import java.time.Instant;

/**
 * @author cflygoo
 */
public class RecordableSubscriptionDescriptor
    extends SimpleSubscriptionDescriptor implements SubscriptionDescriptor {

  private final Instant endTime;

  public RecordableSubscriptionDescriptor(Instant startInstance, Instant endInstant,
      boolean available) {
    super(startInstance, available);
    this.endTime = endInstant;
  }

  public RecordableSubscriptionDescriptor(SubscriptionDescriptor details, Instant endTime) {
    super(details.startTime(), details.isAvailable());
    this.endTime = endTime;
  }

  @Override
  public Instant endTime() {
    return endTime;
  }
}
