package com.apehat.algalon.subscription;

import com.apehat.algalon.subscription.support.subscription.InstantSubscriptionFactory;
import com.apehat.algalon.subscription.support.subscription.SimpleSubscriptionFactory;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author cflygoo
 */
public final class SubscriptionStrategy {

  private static final String TIME_LIMIT_DESCRIPTOR = "timeLimit";
  private static final String SIMPLE_DESCRIPTOR = "simple";

  private static final Map<String, SubscriptionStrategy> CACHE = new ConcurrentHashMap<>();

  static {
    of(TIME_LIMIT_DESCRIPTOR, InstantSubscriptionFactory.getInstance());
    of(SIMPLE_DESCRIPTOR, SimpleSubscriptionFactory.getInstance());
  }

  private final String description;
  private final SubscriptionFactory subscriptionFactory;

  private SubscriptionStrategy(String description, SubscriptionFactory subscriptionFactory) {
    this.description = description;
    this.subscriptionFactory = subscriptionFactory;
  }

  public static SubscriptionStrategy timeLimit() {
    return CACHE.get(TIME_LIMIT_DESCRIPTOR);
  }

  public static SubscriptionStrategy simple() {
    return CACHE.get(SIMPLE_DESCRIPTOR);
  }

  public static SubscriptionStrategy with(String description) {
    return CACHE.get(description);
  }

  public static SubscriptionStrategy of(
      String description, SubscriptionFactory subscriptionFactory) {
    SubscriptionStrategy strategy = with(description);
    if (strategy != null) {
      if (!strategy.getSubscriptionFactory().equals(subscriptionFactory)) {
        throw new IllegalStateException("Subscription strategy " + description + " already exists");
      }
      return strategy;
    }
    strategy = CACHE
        .putIfAbsent(description, new SubscriptionStrategy(description, subscriptionFactory));
    if (strategy != null && !strategy.getSubscriptionFactory().equals(subscriptionFactory)) {
      throw new IllegalStateException("Subscription strategy " + description + " already exists");
    }
    return with(description);
  }

  public String getDescription() {
    return description;
  }

  private SubscriptionFactory getSubscriptionFactory() {
    return subscriptionFactory;
  }

  public Subscription provisionSubscription(Topic topic, boolean initEnable) {
    return initEnable ? getSubscriptionFactory().provisionSubscriptionMayActivated(topic)
        : getSubscriptionFactory().provisionSubscriptionMayInactivated(topic);
  }
}
