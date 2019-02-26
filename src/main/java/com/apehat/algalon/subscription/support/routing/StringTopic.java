package com.apehat.algalon.subscription.support.routing;

import com.apehat.algalon.subscription.Topic;
import java.util.Objects;

/**
 * @author cflygoo
 */
public final class StringTopic implements Topic {

  private static final long serialVersionUID = 3408083461037464101L;

  private final String name;

  private StringTopic(String name) {
    this.name = Objects.requireNonNull(name).trim();
  }

  public static StringTopic of(String name) {
    return new StringTopic(name);
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StringTopic that = (StringTopic) o;
    return Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }

  @Override
  public String toString() {
    return name;
  }
}
