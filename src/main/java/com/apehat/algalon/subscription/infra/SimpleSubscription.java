package com.apehat.algalon.subscription.infra;

import com.apehat.algalon.subscription.Subscription;
import com.apehat.algalon.subscription.SubscriptionDescriptor;
import com.apehat.algalon.subscription.Topic;
import java.time.Instant;

/**
 * @author cflygoo
 */
public class SimpleSubscription implements Subscription {

  private final Topic topic;
  private volatile boolean activate;

  public SimpleSubscription(Topic topic, boolean activate) {
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
  public SubscriptionDescriptor at(Instant instant) {
    return new SimpleSubscriptionDescriptor(instant, activate);
  }
}
