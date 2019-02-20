package com.apehat.algalon.subscription;

import com.apehat.algalon.subscription.infra.topic.ClassTopic;
import com.apehat.algalon.subscription.infra.topic.StringTopic;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author cflygoo
 */
public class SubscriptionService {

  private final SubscriberRepository subscriberRepository;

  public SubscriptionService(SubscriberRepository subscriberRepository) {
    this.subscriberRepository = Objects.requireNonNull(subscriberRepository);
  }

  public void registerSubscriber(SubscriberId id) {
    Subscriber subscriber = subscriberOf(id);
    if (subscriber == null) {
      subscriber = new Subscriber(id);
    }
    subscriberRegistry().save(subscriber);
  }

  public void subscribe(SubscriberId id, Class<?> topic) {
    subscribe(id, ClassTopic.of(topic));
  }

  public void subscribe(SubscriberId id, String topic) {
    subscribe(id, StringTopic.of(topic));
  }

  public void unsubscribe(SubscriberId id, Class<?> topic) {
    unsubscribe(id, ClassTopic.of(topic));
  }

  public void unsubscribe(SubscriberId id, String topic) {
    unsubscribe(id, StringTopic.of(topic));
  }

  public Set<SubscriberDescriptor> subscribersOf(Digest digest) {
    Set<Subscriber> subscribers = subscriberRegistry().subscribersOf(digest);
    Set<SubscriberDescriptor> descriptors = new LinkedHashSet<>();
    for (Subscriber subscriber : subscribers) {
      descriptors.add(subscriber.toDescriptor());
    }
    return descriptors;
  }

  private void subscribe(SubscriberId id, Topic topic) {
    Subscriber subscriber = nonNullSubscriberOf(id);
    subscriber.subscribe(topic);
    subscriberRegistry().save(subscriber);
  }

  private void unsubscribe(SubscriberId id, Topic topic) {
    Subscriber subscriber = nonNullSubscriberOf(id);
    subscriber.unsubscribe(topic);
    subscriberRegistry().save(subscriber);
  }

  private Subscriber subscriberOf(SubscriberId id) {
    return subscriberRegistry().find(id);
  }

  private Subscriber nonNullSubscriberOf(SubscriberId id) {
    Subscriber subscriber = subscriberOf(id);
    if (subscriber == null) {
      throw new IllegalArgumentException("Cannot find subscriber " + id);
    }
    return subscriber;
  }

  private SubscriberRepository subscriberRegistry() {
    return subscriberRepository;
  }
}
