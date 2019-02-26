package com.apehat.algalon.subscription.support.subscription;

import com.apehat.algalon.subscription.Subscription;
import com.apehat.algalon.subscription.SubscriptionDescriptor;
import com.apehat.algalon.subscription.Topic;
import com.apehat.algalon.subscription.support.descriptor.RecordableSubscriptionDescriptor;
import com.apehat.algalon.subscription.support.descriptor.SimpleSubscriptionDescriptor;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author cflygoo
 */
public final class InstantSubscription implements Subscription {

  private final Topic topic;
  private final Instant creationTime;
  private final Set<SubscriptionDescriptor> detailsRecords;

  private final ReadWriteLock rwl = new ReentrantReadWriteLock(true);
  private volatile SubscriptionDescriptor lastSubscription;

  InstantSubscription(Topic topic, boolean available) {
    this.creationTime = Instant.now();
    this.topic = Objects.requireNonNull(topic, "topic");
    this.lastSubscription = new SimpleSubscriptionDescriptor(creationTime, available);
    this.detailsRecords = new CopyOnWriteArraySet<>();
  }

  @Override
  public Topic topic() {
    return topic;
  }

  @Override
  public void activate() {
    flush(true);
  }

  @Override
  public void inactivate() {
    flush(false);
  }

  @Override
  public SubscriptionDescriptor descriptorAt(Instant time) {
    if (time.isAfter(Instant.now())) {
      throw new IllegalArgumentException(
          "The time " + time + " shouldn't latter than the calling");
    }

    if (time.isBefore(creationTime)) {
      return provisionCreationDesc();
    }

    rwl.readLock().lock();
    try {
      if (lastSubscription.contains(time)) {
        return provisionLastDesc(time);
      }
    } finally {
      rwl.readLock().unlock();
    }

    for (SubscriptionDescriptor details : detailsRecords) {
      if (details.contains(time)) {
        return details;
      }
    }
    throw new AssertionError("Cannot find descriptorAt " + time);
  }

  private void flush(boolean available) {
    SubscriptionDescriptor desc;
    if ((desc = tryFlushLastSubscription(available)) != null) {
      this.detailsRecords.add(desc);
    }
  }

  private SubscriptionDescriptor tryFlushLastSubscription(boolean available) {
    SubscriptionDescriptor preDesc = null;

    rwl.readLock().lock();
    if (lastSubscription.isAvailable() != available) {
      rwl.readLock().unlock();
      rwl.writeLock().lock();
      try {
        if (lastSubscription.isAvailable() != available) {
          preDesc = lastSubscription;
          untilNextNanoTimeOf(lastSubscription.startTime());
          lastSubscription = new SimpleSubscriptionDescriptor(Instant.now(), available);
        }
        rwl.readLock().lock();
      } finally {
        rwl.writeLock().unlock();
      }
    }
    try {
      return (preDesc == null) ? null
          : ((SimpleSubscriptionDescriptor) preDesc).end(lastSubscription.startTime());
    } finally {
      rwl.readLock().unlock();
    }
  }

  // Must call with with sync
  private SubscriptionDescriptor provisionLastDesc(Instant time) {
    return new RecordableSubscriptionDescriptor(lastSubscription, time);
  }

  private SubscriptionDescriptor provisionCreationDesc() {
    return new RecordableSubscriptionDescriptor(Instant.MIN, creationTime, false);
  }

  @SuppressWarnings("StatementWithEmptyBody")
  private void untilNextNanoTimeOf(Instant time) {
    while (!Instant.now().isAfter(time)) {
      // spin
    }
  }
}
