package com.apehat.algalon.subscription;

import com.apehat.algalon.common.Assertion;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author cflygoo
 */
public final class Subscriber {

  private final SubscriberId id;
  private final Map<Topic, Subscription> subscriptions;
  private final SubscriptionStrategy strategy;

  Subscriber(SubscriberId id, SubscriptionStrategy subscriptionStrategy) {
    this.id = Objects.requireNonNull(id);
    this.strategy = Objects.requireNonNull(subscriptionStrategy);
    this.subscriptions = new ConcurrentHashMap<>();
  }

  void subscribe(Topic topic) {
    provisionSubscription(topic, true);
  }

  void unsubscribe(Topic topic) {
    provisionSubscription(topic, false);
  }

  public boolean isSubscribed(Digest digest, TopicMapper mapper) {
    Assertion.notLaterThanCalling(digest.time());

    final Instant occurTime = digest.time();
    final Topic topic = digest.topic();

    Instant lastOccurTime = Instant.MIN;
    boolean available = false;

    Set<Topic> topics = subscriptions.keySet();
    for (Topic tpc : topics) {
      if (mapper.isMapping(tpc, topic)) {
        SubscriptionDescriptor details = subscriptions.get(tpc).descriptorAt(occurTime);
        if (details.startTime().isAfter(lastOccurTime)) {
          lastOccurTime = details.startTime();
          available = details.isAvailable();
          assert !lastOccurTime.isAfter(occurTime);
        }
      }
    }
    return available;
  }

  Collection<Subscription> allUnavailableSubscriptions() {
    return allUnavailableSubscriptionsAt(Instant.now());
  }

  Collection<Subscription> allUnavailableSubscriptionsAt(Instant time) {
    Collection<Subscription> existsSubscriptions = this.subscriptions.values();
    existsSubscriptions.removeAll(allAvailableSubscriptionsAt(time));
    return Collections.unmodifiableCollection(existsSubscriptions);
  }

  Collection<Subscription> allAvailableSubscriptions() {
    return allAvailableSubscriptionsAt(Instant.now());
  }

  Collection<Subscription> allAvailableSubscriptionsAt(Instant time) {
    Collection<Subscription> existsSubscriptions = this.subscriptions.values();
    Set<Subscription> availableSubscriptions = new HashSet<>();
    for (Subscription subscription : existsSubscriptions) {
      if (subscription.descriptorAt(time).isAvailable()) {
        availableSubscriptions.add(subscription);
      }
    }
    return Collections.unmodifiableSet(availableSubscriptions);
  }

  Collection<Subscription> allSubscriptions() {
    return Collections.unmodifiableCollection(subscriptions.values());
  }

  SubscriberDescriptor toDescriptor() {
    return new SubscriberDescriptor(id);
  }

  public SubscriberId id() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Subscriber that = (Subscriber) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  private void provisionSubscription(Topic topic, boolean initEnable) {
    Subscription subscription = subscriptions.get(topic);
    if (subscription == null) {
      subscriptions.putIfAbsent(topic, strategy.provisionSubscription(topic, initEnable));
      subscription = subscriptions.get(topic);
    }
    assert subscription != null;
    if (subscription.descriptorAt(Instant.now()).isAvailable() != initEnable) {
      if (initEnable) {
        subscription.activate();
      } else {
        subscription.inactivate();
      }
    }
  }
}
