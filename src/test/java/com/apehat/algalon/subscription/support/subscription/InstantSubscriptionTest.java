package com.apehat.algalon.subscription.support.subscription;

import com.apehat.algalon.subscription.AbstractSubscriptionTest;
import com.apehat.algalon.subscription.Subscription;
import com.apehat.algalon.subscription.SubscriptionStrategy;
import com.apehat.algalon.subscription.Topic;
import com.apehat.algalon.subscription.support.routing.ClassTopic;
import org.junit.Test;

/**
 * @author cflygoo
 */
public class InstantSubscriptionTest extends AbstractSubscriptionTest {

  private final boolean initEnable = true;
  private final Topic topic = ClassTopic.of(InstantSubscriptionTest.class);
  private final Subscription subscription = new InstantSubscription(topic, initEnable);

  public InstantSubscriptionTest() {
    super(SubscriptionStrategy.timeLimit());
  }

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