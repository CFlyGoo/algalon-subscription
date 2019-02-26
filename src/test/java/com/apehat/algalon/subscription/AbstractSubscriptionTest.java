package com.apehat.algalon.subscription;

import com.apehat.algalon.subscription.support.routing.ClassTopic;
import com.apehat.algalon.subscription.support.routing.DefaultTopicMapper;
import com.apehat.algalon.subscription.support.routing.StringTopic;
import com.apehat.algalon.subscription.support.SimpleSubscriberRepository;
import java.util.UUID;

/**
 * @author cflygoo
 */
public abstract class AbstractSubscriptionTest {

  private final SubscriberRepository subscriberRepo = new SimpleSubscriberRepository();
  private final TopicMapper topicMapper = new DefaultTopicMapper();
  private final SubscriptionService subscriptionService;

  protected AbstractSubscriptionTest(SubscriptionStrategy strategy) {
    this.subscriptionService = new SubscriptionService(subscriberRepo, topicMapper, strategy);
  }

  public SubscriberId provisionSubscriberIdFixture(String name) {
    return new SubscriberId(name);
  }

  protected String provisionSubscriberNameFixture() {
    return UUID.randomUUID().toString();
  }

  protected SubscriberRepository provisionSubscriberRepositoryFixture() {
    return subscriberRepo;
  }

  protected SubscriptionService provisionSubscriptionServiceFixture() {
    return subscriptionService;
  }

  protected TopicMapper provisionTopicMapperFixture() {
    return topicMapper;
  }

  protected Digest provisionDigestFixture(Class<?> tpc) {
    return Digest.atCurrent(ClassTopic.of(tpc));
  }

  protected Digest provisionDigestFixture(String tpc) {
    return Digest.atCurrent(StringTopic.of(tpc));
  }
}
