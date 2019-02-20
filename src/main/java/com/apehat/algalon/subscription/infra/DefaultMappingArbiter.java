package com.apehat.algalon.subscription.infra;

import com.apehat.algalon.subscription.MappingArbiter;
import com.apehat.algalon.subscription.Topic;

/**
 * @author cflygoo
 */
public final class DefaultMappingArbiter implements MappingArbiter {

  @Override
  public boolean isMapping(Topic t1, Topic t2) {
    return t1.isAssignableFrom(t2);
  }
}
