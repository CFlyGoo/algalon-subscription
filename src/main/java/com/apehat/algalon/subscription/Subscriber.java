package com.apehat.algalon.subscription;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author cflygoo
 */
public final class Subscriber {

  private final SubscriberId id;
  private final SubscriptionFactory subscriptionFactory;
  private final Map<Topic, Subscription> subscriptions;

  public Subscriber(SubscriberId id, SubscriptionFactory subscriptionFactory) {
    this.id = Objects.requireNonNull(id);
    this.subscriptionFactory = Objects.requireNonNull(subscriptionFactory);
    this.subscriptions = new ConcurrentHashMap<>();
  }

  void subscribe(Topic topic) {
    Subscription subscription =
        subscriptionFactory().provisionSubscriptionMayActivated(topic);
    subscription.activate();
    subscriptions.put(topic, subscription);
  }

  void unsubscribe(Topic topic) {
    Subscription subscription =
        subscriptionFactory().provisionSubscriptionMayInactivated(topic);
    subscription.inactivate();
    subscriptions.put(topic, subscription);
  }

  public boolean isSubscribed(Digest digest, TopicMapper mapper) {
    assertNotLaterThanCalling(digest);

    final Instant occurTime = digest.occurTime();
    final Topic topic = digest.topic();

    Instant lastOccurTime = Instant.MIN;
    boolean available = false;

    Set<Topic> topics = subscriptions.keySet();
    for (Topic tpc : topics) {
      if (mapper.isMapping(tpc, topic)) {
        SubscriptionDescriptor details = subscriptions.get(tpc).at(occurTime);
        if (details.startTime().isAfter(lastOccurTime)) {
          lastOccurTime = details.startTime();
          available = details.isAvailable();
          assert !lastOccurTime.isAfter(occurTime);
        }
      }
    }
    return available;
  }

  private SubscriptionFactory subscriptionFactory() {
    return subscriptionFactory;
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

  protected final void assertNotLaterThanCalling(Digest digest) {
    Instant now = Instant.now();
    if (digest.occurTime().isAfter(now)) {
      throw new IllegalArgumentException("The timestamp cannot be latter than the " + now);
    }
  }
}
