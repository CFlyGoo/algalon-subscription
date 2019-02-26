package com.apehat.algalon.subscription.infra;

import com.apehat.algalon.subscription.Subscription;
import com.apehat.algalon.subscription.SubscriptionFactory;
import com.apehat.algalon.subscription.Topic;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author cflygoo
 */
public abstract class AbstractSubscriptionFactory implements SubscriptionFactory {

  private final Map<Topic, Subscription> cache = new ConcurrentHashMap<>();

  @Override
  public Subscription provisionSubscriptionMayActivated(Topic topic) {
    return provisionSubscription(topic, true);
  }

  @Override
  public Subscription provisionSubscriptionMayInactivated(Topic topic) {
    return provisionSubscription(topic, false);
  }

  protected abstract Subscription newSubscription(Topic topic, boolean initEnable);

  private void save(Subscription subscription) {
    this.cache.putIfAbsent(subscription.topic(), subscription);
  }

  private Subscription provisionSubscription(Topic topic, boolean initEnable) {
    Subscription subscription = cache.get(topic);
    if (subscription == null) {
      save(newSubscription(topic, initEnable));
      subscription = cache.get(topic);
    }
    return subscription;
  }

}
