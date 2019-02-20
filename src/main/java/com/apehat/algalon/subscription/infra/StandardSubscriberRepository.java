package com.apehat.algalon.subscription.infra;

import com.apehat.algalon.subscription.Digest;
import com.apehat.algalon.subscription.Subscriber;
import com.apehat.algalon.subscription.SubscriberId;
import com.apehat.algalon.subscription.SubscriberRepository;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author cflygoo
 */
public class StandardSubscriberRepository implements SubscriberRepository {

  private final Map<SubscriberId, Subscriber> subscribers = new ConcurrentHashMap<>();

  @Override
  public void save(Subscriber subscriber) {
    subscribers.put(subscriber.id(), subscriber);
  }

  @Override
  public Subscriber find(SubscriberId id) {
    return subscribers.get(id);
  }

  @Override
  public Set<Subscriber> subscribersOf(Digest digest) {
    Set<Subscriber> availableSubscribers = new LinkedHashSet<>();
    Collection<Subscriber> candidateSubscribers = subscribers();
    for (Subscriber subscriber : candidateSubscribers) {
      if (subscriber.isSubscribed(digest)) {
        availableSubscribers.add(subscriber);
      }
    }
    return availableSubscribers;
  }

  private Collection<Subscriber> subscribers() {
    return this.subscribers.values();
  }
}
