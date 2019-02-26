package com.apehat.algalon.subscription;

import com.apehat.algalon.subscription.support.routing.ClassTopic;
import com.apehat.algalon.subscription.support.routing.StringTopic;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author cflygoo
 */
public class SubscriptionService {

  private final SubscriberRepository subscriberRepository;
  private final TopicMapper topicMapper;
  private final SubscriptionStrategy strategy;

  public SubscriptionService(SubscriberRepository subscriberRepository,
      TopicMapper topicMapper, SubscriptionStrategy subscriptionStrategy) {
    this.subscriberRepository = Objects.requireNonNull(subscriberRepository);
    this.topicMapper = Objects.requireNonNull(topicMapper);
    this.strategy = Objects.requireNonNull(subscriptionStrategy);
  }

  public void registerSubscriber(String name) {
    SubscriberId id = new SubscriberId(name);
    if (subscriberOf(id) != null) {
      throw new IllegalStateException("Subscriber " + id + " already exists");
    }
    subscriberRepo().save(new Subscriber(id, strategy));
  }

  public void subscribe(String name, Class<?> topic) {
    subscribe(name, ClassTopic.of(topic));
  }

  public void subscribe(String name, String topic) {
    subscribe(name, StringTopic.of(topic));
  }

  public void unsubscribe(String name, Class<?> topic) {
    unsubscribe(name, ClassTopic.of(topic));
  }

  public void unsubscribe(String name, String topic) {
    unsubscribe(name, StringTopic.of(topic));
  }

  public Set<SubscriberDescriptor> subscribersOf(Topic topic) {
    return subscribersOf(Digest.atCurrent(topic));
  }

  public Set<SubscriberDescriptor> subscribersOf(Digest digest) {
    Collection<Subscriber> subscribers = subscriberRepo().subscribersOf(digest, topicMapper);
    Set<SubscriberDescriptor> descriptors = new LinkedHashSet<>();
    for (Subscriber subscriber : subscribers) {
      descriptors.add(subscriber.toDescriptor());
    }
    return descriptors;
  }

  private void subscribe(String name, Topic topic) {
    SubscriberId id = new SubscriberId(name);
    Subscriber subscriber = nonNullSubscriberOf(id);
    subscriber.subscribe(topic);
    subscriberRepo().save(subscriber);
  }

  private void unsubscribe(String name, Topic topic) {
    SubscriberId id = new SubscriberId(name);
    Subscriber subscriber = nonNullSubscriberOf(id);
    subscriber.unsubscribe(topic);
    subscriberRepo().save(subscriber);
  }

  private Subscriber subscriberOf(SubscriberId id) {
    return subscriberRepo().find(id);
  }

  private Subscriber nonNullSubscriberOf(SubscriberId id) {
    Subscriber subscriber = subscriberOf(id);
    if (subscriber == null) {
      throw new IllegalArgumentException("Cannot find subscriber " + id);
    }
    return subscriber;
  }

  private SubscriberRepository subscriberRepo() {
    return subscriberRepository;
  }
}
