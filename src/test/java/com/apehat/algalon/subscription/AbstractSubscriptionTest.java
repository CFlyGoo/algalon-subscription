package com.apehat.algalon.subscription;

import com.apehat.algalon.subscription.infra.StandardSubscriberRepository;
import com.apehat.algalon.subscription.infra.topic.ClassTopic;
import com.apehat.algalon.subscription.infra.topic.StringTopic;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * @author cflygoo
 */
public abstract class AbstractSubscriptionTest {

  private final SubscriberRepository subscriberRepo = new StandardSubscriberRepository();
  private final SubscriptionService subscriptionService = new SubscriptionService(subscriberRepo);

  protected SubscriberId provisionSubscriberIdFixture() {
    return new MockSubscriberId(UUID.randomUUID().toString());
  }

  protected SubscriberRepository provisionSubscriberRepositoryFixture() {
    return subscriberRepo;
  }

  protected SubscriptionService provisionSubscriptionServiceFixture() {
    return subscriptionService;
  }

  protected Digest provisionDigestFixture(Class<?> tpc) {
    return new Digest(ClassTopic.of(tpc), Instant.now());
  }

  protected Digest provisionDigestFixture(String tpc) {
    return new Digest(StringTopic.of(tpc), Instant.now());
  }

  private static final class MockSubscriberId implements SubscriberId {

    private static final long serialVersionUID = -5562347513051056520L;

    private final String id;

    private MockSubscriberId(String id) {
      this.id = id;
    }

    @Override
    public String toString() {
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
      MockSubscriberId that = (MockSubscriberId) o;
      return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
      return Objects.hash(id);
    }
  }
}
