package com.apehat.algalon.subscription.support.routing;

import com.apehat.algalon.subscription.Topic;
import com.apehat.algalon.subscription.TopicMapper;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author cflygoo
 */
public class StringTopicMapper implements TopicMapper {

  private final Map<StringTopic, Set<Topic>> maps = new ConcurrentHashMap<>();

  public void removeMapping(String primary, String map) {
    removeMapping(StringTopic.of(primary), StringTopic.of(map));
  }

  public void removeMapping(String primary, Topic topic) {
    removeMapping(StringTopic.of(primary), topic);
  }

  public void removeMapping(StringTopic primary, Topic topic) {
    if (primary.equals(topic)) {
      throw new IllegalArgumentException("Cannot remove mapping with self of " + primary);
    }
    maps.get(primary).remove(topic);
  }

  public void addMapping(String primary, String map) {
    addMapping(StringTopic.of(primary), StringTopic.of(map));
  }

  public void addMapping(String primary, Topic map) {
    addMapping(StringTopic.of(primary), map);
  }

  public void addMapping(StringTopic primary, Topic map) {
    if (primary.equals(map)) {
      return;
    }
    Set<Topic> topics = maps.get(primary);
    if (topics == null) {
      maps.putIfAbsent(primary, new CopyOnWriteArraySet<>());
      topics = maps.get(primary);
    }
    topics.add(map);
  }

  @Override
  public boolean isMapping(Topic t1, Topic t2) {
    if (t1.equals(t2)) {
      return true;
    }
    if (!(t1 instanceof StringTopic)) {
      return false;
    }
    return maps.get(t1).contains(t2);
  }
}
