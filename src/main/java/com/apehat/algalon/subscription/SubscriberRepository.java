package com.apehat.algalon.subscription;

import java.util.Collection;

/**
 * @author cflygoo
 */
public interface SubscriberRepository {

  void save(Subscriber subscriber);

  Subscriber find(SubscriberId id);

  Collection<Subscriber> subscribersOf(Digest digest, TopicMapper topicMapper);
}
