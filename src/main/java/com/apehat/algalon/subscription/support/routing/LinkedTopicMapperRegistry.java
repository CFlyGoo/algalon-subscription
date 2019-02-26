package com.apehat.algalon.subscription.support.routing;

import com.apehat.algalon.subscription.Topic;
import com.apehat.algalon.subscription.TopicMapper;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author cflygoo
 */
public class LinkedTopicMapperRegistry implements TopicMapper {

  private final Set<TopicMapper> mappers = new LinkedHashSet<>();

  public void registerMapper(TopicMapper mapper) {
    this.mappers.add(mapper);
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
