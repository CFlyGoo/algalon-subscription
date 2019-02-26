package com.apehat.algalon.subscription;

/**
 * @author cflygoo
 */
public interface SubscriptionFactory {

  Subscription provisionSubscriptionMayActivated(Topic topic);

  Subscription provisionSubscriptionMayInactivated(Topic topic);

}
