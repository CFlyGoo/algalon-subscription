package com.apehat.algalon.subscription.infra;

import com.apehat.algalon.subscription.AbstractSubscriptionTest;
import com.apehat.algalon.subscription.Subscription;
import com.apehat.algalon.subscription.Topic;
import com.apehat.algalon.subscription.infra.topic.ClassTopic;
import org.junit.Test;

/**
 * @author cflygoo
 */
public class InstantSubscriptionTest extends AbstractSubscriptionTest {

  private final boolean initEnable = true;
  private final Topic topic = ClassTopic.of(InstantSubscriptionTest.class);
  private final Subscription subscription = new InstantSubscription(topic, initEnable);

  @Test
  public void concurrentTest() {
    for (int i = 0; i < 1000; i++) {
      if (i % 3 == 0) {
        new Thread(new Inactivate(subscription)).start();
      } else {
        new Thread(new Activate(subscription)).start();
      }
    }
  }

  private static final class Inactivate implements Runnable {

    private final Subscription subscription;

    private Inactivate(Subscription subscription) {
      this.subscription = subscription;
    }

    @Override
    public void run() {
      subscription.inactivate();
    }
  }

  private static final class Activate implements Runnable {

    private final Subscription subscription;

    private Activate(Subscription subscription) {
      this.subscription = subscription;
    }

    @Override
    public void run() {
      subscription.activate();
    }
  }

}