package com.summer.qa.async;

import com.alibaba.fastjson.JSONObject;
import com.summer.qa.util.JedisAdapter;
import com.summer.qa.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author ：lightingSummer
 * @date ：2019/6/11 0011
 * @description：
 */
@Component
public class EventProducer {
  private static final Logger logger = LoggerFactory.getLogger(EventProducer.class);
  /** rabbitmq */
  /*
    @Autowired private AmqpTemplate rabbitTemplate;

    public boolean addEvent(EventModel event) {
      try {
        String msg = JSONObject.toJSONString(event);
        this.rabbitTemplate.convertAndSend(RabbitMqConfig.QUEUE, msg);
        return true;
      } catch (Exception e) {
        logger.error("add event error " + e.getMessage());
        return false;
      }
    }
  */

  @Autowired private JedisAdapter jedisAdapter;

  public boolean addEvent(EventModel event) {
    try {
      String listValue = JSONObject.toJSONString(event);
      String listKey = RedisKeyUtil.getEventQueueKey();
      jedisAdapter.lpush(listKey, listValue);
      return true;
    } catch (Exception e) {
      logger.error("add event error " + e.getMessage());
      return false;
    }
  }
}
