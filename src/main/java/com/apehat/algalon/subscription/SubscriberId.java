package com.apehat.algalon.subscription;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author cflygoo
 */
public final class SubscriberId implements Serializable {

  private static final long serialVersionUID = -149048355806721634L;

  private final String value;

  public SubscriberId(String value) {
    this.value = Objects.requireNonNull(value);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SubscriberId that = (SubscriberId) o;
    return Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  @Override
  public String toString() {
    return value;
  }

}
