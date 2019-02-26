package com.apehat.algalon.subscription.support.subscription;

import com.apehat.algalon.subscription.Subscription;
import com.apehat.algalon.subscription.Topic;

/**
 * @author cflygoo
 */
public class SimpleSubscriptionFactory extends AbstractSubscriptionFactory {

  private static final SimpleSubscriptionFactory INSTANCE = new SimpleSubscriptionFactory();

  private SimpleSubscriptionFactory() {
  }

  public static SimpleSubscriptionFactory getInstance() {
    return INSTANCE;
  }

  @Override
  protected Subscription newSubscription(Topic topic, boolean initEnable) {
    return new SimpleSubscription(topic, initEnable);
  }
}
