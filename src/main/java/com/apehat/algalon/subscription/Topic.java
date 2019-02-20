package com.apehat.algalon.subscription;

import java.io.Serializable;

/**
 * @author cflygoo
 */
public interface Topic extends Serializable {

  String name();

  boolean isAssignableFrom(Topic topic);
}
