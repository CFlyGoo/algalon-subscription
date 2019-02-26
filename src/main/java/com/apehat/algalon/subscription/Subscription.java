package com.apehat.algalon.subscription;

import java.time.Instant;

/**
 * @author cflygoo
 */
public interface Subscription {

  Topic topic();

  void activate();

  void inactivate();

  SubscriptionDescriptor descriptorAt(Instant instant);
}
