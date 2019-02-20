package com.apehat.algalon.subscription;

import java.util.Set;

/**
 * @author cflygoo
 */
public interface SubscriberRepository {

  void save(Subscriber subscriber);

  Subscriber find(SubscriberId id);

  Set<Subscriber> subscribersOf(Digest digest);
}
