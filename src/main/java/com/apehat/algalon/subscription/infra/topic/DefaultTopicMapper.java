package com.apehat.algalon.subscription.infra.topic;

import com.apehat.algalon.subscription.Topic;
import com.apehat.algalon.subscription.TopicMapper;
import java.util.HashSet;
import java.util.Set;

/**
 * @author cflygoo
 */
public final class DefaultTopicMapper implements TopicMapper {

  private final Set<TopicMapper> mappers = new HashSet<>();

  public DefaultTopicMapper() {
    mappers.add(new ClassTopicMapper());
    mappers.add(new StringTopicMapper());
  }

  @Override
  public boolean isMapping(Topic t1, Topic t2) {
    for (TopicMapper mapper : mappers) {
      boolean mapping = mapper.isMapping(t1, t2);
      if (mapping) {
        return true;
      }
    }
    return false;
  }
}
