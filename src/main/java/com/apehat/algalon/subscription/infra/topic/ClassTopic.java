package com.apehat.algalon.subscription.infra.topic;

import com.apehat.algalon.subscription.Topic;
import java.util.Objects;

/**
 * @author cflygoo
 */
public final class ClassTopic implements Topic {

  private static final long serialVersionUID = 7841105530243823104L;

  private final Class<?> klass;

  private ClassTopic(Class<?> klass) {
    this.klass = Objects.requireNonNull(klass);
  }

  public static ClassTopic of(Class<?> klass) {
    return new ClassTopic(klass);
  }

  @Override
  public String name() {
    return klass.getName();
  }

  public Class<?> source() {
    return klass;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ClassTopic that = (ClassTopic) o;
    return Objects.equals(klass, that.klass);
  }

  @Override
  public int hashCode() {
    return Objects.hash(klass);
  }

  @Override
  public String toString() {
    return name();
  }
}
