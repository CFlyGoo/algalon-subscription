package com.apehat.algalon.subscription;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.Instant;
import org.junit.Test;

/**
 * @author cflygoo
 */
public class SubscriptionServiceTest extends AbstractSubscriptionTest {

  private static final Class<?> CLASS_TOPIC = SubscriptionServiceTest.class;
  private static final String STRING_TOPIC = CLASS_TOPIC.getSimpleName();

  @Test
  public void provisionWithNotExistsSubscriber() {
    SubscriberId id = register();
    Subscriber subscriber = provisionSubscriberRepositoryFixture().find(id);
    assertNotNull(subscriber);
    assertEquals(id, subscriber.id());
  }

  @Test
  public void subscribeByClass() {
    SubscriberId id = register();
    Pair pair = subscribeWithClass(id);

    Subscriber subscriber = provisionSubscriberRepositoryFixture().find(id);
    assertFalse(subscriber.isSubscribed(pair.frontDigest));
    assertTrue(subscriber.isSubscribed(pair.rearDigest));
  }

  @Test
  public void subscribeByString() {
    SubscriberId id = register();
    Pair pair = subscribeWithString(id);

    Subscriber subscriber = provisionSubscriberRepositoryFixture().find(id);
    assertFalse(subscriber.isSubscribed(pair.frontDigest));
    assertTrue(subscriber.isSubscribed(pair.rearDigest));
  }

  @Test(expected = IllegalArgumentException.class)
  public void subscribeByClassWithIllegalId() {
    SubscriberId id = provisionSubscriberIdFixture();
    provisionSubscriptionServiceFixture().subscribe(id, CLASS_TOPIC);
  }

  @Test(expected = IllegalArgumentException.class)
  public void subscribeByStringWithIllegalId() {
    SubscriberId id = provisionSubscriberIdFixture();
    provisionSubscriptionServiceFixture().subscribe(id, STRING_TOPIC);
  }

  @Test
  public void unsubscribeByClass() {
  }

  @Test
  public void unsubscribeByString() {
  }

  @Test
  public void subscribersOf() {
  }

  private Pair subscribeWithClass(SubscriberId id) {
    Digest frontDigest = provisionDigestFixture(CLASS_TOPIC);
    untilNextNanoTimeOf(frontDigest.occurTime());

    provisionSubscriptionServiceFixture().subscribe(id, CLASS_TOPIC);
    Digest rearDigest = provisionDigestFixture(CLASS_TOPIC);
    return new Pair(frontDigest, rearDigest);
  }

  private Pair subscribeWithString(SubscriberId id) {
    Digest frontDigest = provisionDigestFixture(STRING_TOPIC);
    untilNextNanoTimeOf(frontDigest.occurTime());
    provisionSubscriptionServiceFixture().subscribe(id, STRING_TOPIC);
    Digest rearDigest = provisionDigestFixture(STRING_TOPIC);
    return new Pair(frontDigest, rearDigest);
  }

  @SuppressWarnings("StatementWithEmptyBody")
  private void untilNextNanoTimeOf(Instant time) {
    while (!Instant.now().isAfter(time)) {
      // spin
    }
  }

  private SubscriberId register() {
    SubscriberId id = provisionSubscriberIdFixture();
    provisionSubscriptionServiceFixture().registerSubscriber(id);
    return id;
  }

  private static final class Pair {

    final Digest frontDigest;
    final Digest rearDigest;

    private Pair(Digest frontDigest, Digest rearDigest) {
      this.frontDigest = frontDigest;
      this.rearDigest = rearDigest;
    }
  }
}