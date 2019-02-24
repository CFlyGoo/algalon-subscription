package com.apehat.algalon.subscription.infra.topic;

import com.apehat.algalon.subscription.Topic;
import com.apehat.algalon.subscription.TopicMapper;

/**
 * @author cflygoo
 */
public class ClassTopicMapper implements TopicMapper {

  @Override
  public boolean isMapping(Topic t1, Topic t2) {
    return t1 instanceof ClassTopic
        && t2 instanceof ClassTopic
        && ((ClassTopic) t1).source().isAssignableFrom(((ClassTopic) t2).source());
  }
}
