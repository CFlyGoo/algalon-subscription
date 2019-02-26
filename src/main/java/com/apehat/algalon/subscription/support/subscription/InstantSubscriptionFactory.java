package com.apehat.algalon.subscription.support.subscription;

import com.apehat.algalon.subscription.Subscription;
import com.apehat.algalon.subscription.SubscriptionFactory;
import com.apehat.algalon.subscription.Topic;

/**
 * @author cflygoo
 */
public class InstantSubscriptionFactory extends AbstractSubscriptionFactory {

  private static final InstantSubscriptionFactory INSTANCE = new InstantSubscriptionFactory();

  private InstantSubscriptionFactory() {
  }

  public static SubscriptionFactory getInstance() {
    return INSTANCE;
  }

  @Override
  protected Subscription newSubscription(Topic topic, boolean initEnable) {
    return new InstantSubscription(topic, initEnable);
  }
}
