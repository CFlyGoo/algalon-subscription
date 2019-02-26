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

  public SubscriptionServiceTest() {
    super(SubscriptionStrategy.timeLimit());
  }

  @Test
  public void provisionWithNotExistsSubscriber() {
    String name = register();
    SubscriberId id = provisionSubscriberIdFixture(name);
    Subscriber subscriber = provisionSubscriberRepositoryFixture().find(id);
    assertNotNull(subscriber);
    assertEquals(id, subscriber.id());
  }

  @Test
  public void subscribeByClass() {
    String name = register();
    Pair pair = subscribeWithClass(name);

    Subscriber subscriber = provisionSubscriberRepositoryFixture()
        .find(provisionSubscriberIdFixture(name));
    assertFalse(subscriber.isSubscribed(pair.frontDigest, provisionTopicMapperFixture()));
    assertTrue(subscriber.isSubscribed(pair.rearDigest, provisionTopicMapperFixture()));
  }

  @Test
  public void subscribeByString() {
    String name = register();
    Pair pair = subscribeWithString(name);

    Subscriber subscriber = provisionSubscriberRepositoryFixture()
        .find(provisionSubscriberIdFixture(name));
    assertFalse(subscriber.isSubscribed(pair.frontDigest, provisionTopicMapperFixture()));
    assertTrue(subscriber.isSubscribed(pair.rearDigest, provisionTopicMapperFixture()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void subscribeByClassWithIllegalId() {
    provisionSubscriptionServiceFixture().subscribe(provisionSubscriberNameFixture(), CLASS_TOPIC);
  }

  @Test(expected = IllegalArgumentException.class)
  public void subscribeByStringWithIllegalId() {
    String name = provisionSubscriberNameFixture();
    provisionSubscriptionServiceFixture().subscribe(name, STRING_TOPIC);
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

  private Pair subscribeWithClass(String name) {
    Digest frontDigest = provisionDigestFixture(CLASS_TOPIC);
    untilNextNanoTimeOf(frontDigest.time());

    provisionSubscriptionServiceFixture().subscribe(name, CLASS_TOPIC);
    Digest rearDigest = provisionDigestFixture(CLASS_TOPIC);
    return new Pair(frontDigest, rearDigest);
  }

  private Pair subscribeWithString(String name) {
    Digest frontDigest = provisionDigestFixture(STRING_TOPIC);
    untilNextNanoTimeOf(frontDigest.time());
    provisionSubscriptionServiceFixture().subscribe(name, STRING_TOPIC);
    Digest rearDigest = provisionDigestFixture(STRING_TOPIC);
    return new Pair(frontDigest, rearDigest);
  }

  @SuppressWarnings("StatementWithEmptyBody")
  private void untilNextNanoTimeOf(Instant time) {
    while (!Instant.now().isAfter(time)) {
      // spin
    }
  }

  private String register() {
    String name = provisionSubscriberNameFixture();
    provisionSubscriptionServiceFixture().registerSubscriber(name);
    return name;
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