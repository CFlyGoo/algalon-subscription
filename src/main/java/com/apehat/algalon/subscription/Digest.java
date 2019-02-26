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
  private final Instant time;

  public static Digest atCurrent(Topic topic) {
    return new Digest(topic, Instant.now());
  }

  public static Digest of(Topic topic, Instant time) {
    return new Digest(topic, time);
  }

  private Digest(Topic topic, Instant time) {
    this.topic = Objects.requireNonNull(topic);
    this.time = time;
  }

  public Topic topic() {
    return topic;
  }

  public Instant time() {
    return time;
  }

  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Digest digest = (Digest) o;
    return time().equals(digest.time()) &&
        Objects.equals(topic, digest.topic);
  }

  @Override
  public int hashCode() {
    return Objects.hash(topic, time);
  }

  @Override
  public String toString() {
    return topic + ":" + time;
  }
}
