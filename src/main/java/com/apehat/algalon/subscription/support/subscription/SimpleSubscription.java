package com.apehat.algalon.subscription.support.subscription;

import com.apehat.algalon.subscription.Subscription;
import com.apehat.algalon.subscription.SubscriptionDescriptor;
import com.apehat.algalon.subscription.Topic;
import com.apehat.algalon.subscription.support.descriptor.SimpleSubscriptionDescriptor;
import java.time.Instant;

/**
 * @author cflygoo
 */
public class SimpleSubscription implements Subscription {

  private final Topic topic;
  private volatile boolean activate;

  SimpleSubscription(Topic topic, boolean activate) {
    this.topic = topic;
    this.activate = activate;
  }

  @Override
  public Topic topic() {
    return topic;
  }

  @Override
  public void activate() {
    this.activate = true;
  }

  @Override
  public void inactivate() {
    this.activate = false;
  }

  @Override
  public SubscriptionDescriptor descriptorAt(Instant instant) {
    return new SimpleSubscriptionDescriptor(instant, activate);
  }
}
