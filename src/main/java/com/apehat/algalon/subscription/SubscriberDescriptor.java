package com.apehat.algalon.subscription;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author cflygoo
 */
public final class SubscriberDescriptor implements Serializable {

  private static final long serialVersionUID = 6925957427806759004L;

  private final SubscriberId id;

  SubscriberDescriptor(SubscriberId id) {
    this.id = Objects.requireNonNull(id);
  }

  public SubscriberId id() {
    return id;
  }
}
