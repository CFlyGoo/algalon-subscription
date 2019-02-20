package com.apehat.algalon.subscription;

import com.apehat.algalon.subscription.infra.StandardSubscriptions;
import java.util.Objects;

/**
 * @author cflygoo
 */
public final class Subscriber {

  private final SubscriberId id;
  private final Subscriptions subscriptions;

  Subscriber(SubscriberId id) {
    this(id, new StandardSubscriptions());
  }

  private Subscriber(SubscriberId id, Subscriptions subscription) {
    this.id = Objects.requireNonNull(id);
    this.subscriptions = Objects.requireNonNull(subscription);
  }

  void subscribe(Topic topic) {
    subscriptions().provisionSubscriptionMayActivated(topic).activate();
  }

  void unsubscribe(Topic topic) {
    subscriptions().provisionSubscriptionMayInactivated(topic).activate();
  }

  public boolean isSubscribed(Digest digest) {
    return subscriptions().isSubscribed(digest);
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

  private Subscriptions subscriptions() {
    return this.subscriptions;
  }
}
