package com.apehat.algalon.subscription;

/**
 * @author cflygoo
 */
public interface Subscriptions {

  boolean isSubscribed(Digest digest);

  Subscription provisionSubscriptionMayActivated(Topic topic);

  Subscription provisionSubscriptionMayInactivated(Topic topic);
}
