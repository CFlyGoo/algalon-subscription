package com.apehat.algalon.subscription.support.routing;

/**
 * @author cflygoo
 */
public final class DefaultTopicMapper extends LinkedTopicMapperRegistry {

  public DefaultTopicMapper() {
    registerMapper(new ClassTopicMapper());
    registerMapper(new StringTopicMapper());
  }
}