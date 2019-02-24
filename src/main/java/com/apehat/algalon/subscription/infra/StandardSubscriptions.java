package com.apehat.algalon.subscription.infra;

import com.apehat.algalon.subscription.Digest;
import com.apehat.algalon.subscription.Subscription;
import com.apehat.algalon.subscription.SubscriptionDescriptor;
import com.apehat.algalon.subscription.Subscriptions;
import com.apehat.algalon.subscription.Topic;
import com.apehat.algalon.subscription.TopicMapper;
import com.apehat.algalon.subscription.infra.topic.DefaultTopicMapper;
import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author cflygoo
 */
public class StandardSubscriptions implements Subscriptions {

  private static final TopicMapper DEFAULT_TOPIC_MAPPER = new DefaultTopicMapper();

  private final Map<Topic, Subscription> subscriptions = new ConcurrentHashMap<>();
  private TopicMapper mapper;

  public StandardSubscriptions() {
    this(DEFAULT_TOPIC_MAPPER);
  }

  public StandardSubscriptions(TopicMapper mapper) {
    this.mapper = mapper;
  }

  @Override
  public boolean isSubscribed(Digest digest) {
    assertNotLaterThanCalling(digest);

    final Instant occurTime = digest.occurTime();
    final Topic topic = digest.topic();

    Instant lastOccurTime = Instant.MIN;
    boolean available = false;

    Set<Topic> topics = subscriptions.keySet();
    for (Topic tpc : topics) {
      if (!arbiter().isMapping(tpc, topic)) {
        continue;
      }
      SubscriptionDescriptor details = subscriptions.get(tpc).at(occurTime);
      if (details.startTime().isAfter(lastOccurTime)) {
        lastOccurTime = details.startTime();
        available = details.isAvailable();
        assert !lastOccurTime.isAfter(occurTime);
      }
    }
    return available;
  }

  public Subscription provisionSubscriptionMayActivated(Topic topic) {
    return provisionSubscription(topic, true);
  }

  public Subscription provisionSubscriptionMayInactivated(Topic topic) {
    return provisionSubscription(topic, false);
  }

  public TopicMapper getTopicMapper() {
    return mapper;
  }

  private void save(Subscription subscription) {
    this.subscriptions.putIfAbsent(subscription.topic(), subscription);
  }

  private void assertNotLaterThanCalling(Digest digest) {
    Instant now = Instant.now();
    if (digest.occurTime().isAfter(now)) {
      throw new IllegalArgumentException("The timestamp cannot be latter than the " + now);
    }
  }

  private Subscription provisionSubscription(Topic topic, boolean initEnable) {
    Subscription subscription = subscriptions.get(topic);
    if (subscription == null) {
      save(new InstantSubscription(topic, initEnable));
      subscription = subscriptions.get(topic);
    }
    return subscription;
  }

  private TopicMapper arbiter() {
    return mapper;
  }
}
