package com.apehat.algalon.subscription;

import com.apehat.algalon.subscription.infra.InstantSubscriptionFactory;
import com.apehat.algalon.subscription.infra.SimpleSubscriptionFactory;
import com.apehat.algalon.subscription.routing.ClassTopic;
import com.apehat.algalon.subscription.routing.StringTopic;
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

  public SubscriptionService(SubscriberRepository subscriberRepository, TopicMapper topicMapper) {
    this.subscriberRepository = Objects.requireNonNull(subscriberRepository);
    this.topicMapper = Objects.requireNonNull(topicMapper);
  }

  public void registerTimeLimitSubscriber(SubscriberId id) {
    if (subscriberOf(id) != null) {
      throw new IllegalStateException("Subscriber " + id + " already exists");
    }
    subscriberRegistry().save(new Subscriber(id, InstantSubscriptionFactory.getInstance()));
  }

  public void registerSubscriber(SubscriberId id) {
    if (subscriberOf(id) != null) {
      throw new IllegalStateException("Subscriber " + id + " already exists");
    }
    subscriberRegistry().save(new Subscriber(id, SimpleSubscriptionFactory.getInstance()));
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
    Collection<Subscriber> subscribers = subscriberRegistry().subscribersOf(digest, topicMapper);
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
