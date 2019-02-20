package com.apehat.algalon.subscription;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * @author cflygoo
 */
public final class Digest implements Serializable {

  private static final long serialVersionUID = 2527727545469124790L;

  private final Topic topic;
  private final Instant occurTime;

  public Digest(Topic topic, Instant occurredOn) {
    this.topic = Objects.requireNonNull(topic);
    this.occurTime = occurredOn;
  }

  public Topic topic() {
    return topic;
  }

  public Instant occurTime() {
    return occurTime;
  }

  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Digest digest = (Digest) o;
    return occurTime().equals(digest.occurTime()) &&
        Objects.equals(topic, digest.topic);
  }

  @Override
  public int hashCode() {
    return Objects.hash(topic, occurTime);
  }

  @Override
  public String toString() {
    return topic + ":" + occurTime;
  }
}
