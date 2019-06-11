package com.summer.qa.async;

import com.alibaba.fastjson.JSON;
import com.summer.qa.util.JedisAdapter;
import com.summer.qa.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：lightingSummer
 * @date ：2019/6/11 0011
 * @description：
 */
@Component
public class EventConsumer implements InitializingBean, ApplicationContextAware {
  private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);

  @Autowired private JedisAdapter jedisAdapter;

  private ApplicationContext applicationContext;
  private Map<EventType, List<EventHandler>> config = new HashMap<>();

  @Override
  public void afterPropertiesSet() throws Exception {
    // 获取所有的事件处理的beans
    Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
    if (beans != null) {
      for (Map.Entry<String, EventHandler> entry : beans.entrySet()) {
        List<EventType> eventTypes = entry.getValue().getSupportEventTypes();
        // 遍历加入事件
        for (EventType eventType : eventTypes) {
          if (!config.containsKey(eventType)) {
            config.put(eventType, new ArrayList<EventHandler>());
          }
          config.get(eventType).add(entry.getValue());
        }
      }
    }

    Thread thread =
        new Thread(
            () -> {
              while (true) {
                String key = RedisKeyUtil.getEventQueueKey();
                List<String> events = jedisAdapter.brpop(0, key);

                for (String message : events) {
                  if (message.equals(key)) {
                    continue;
                  }

                  EventModel eventModel = JSON.parseObject(message, EventModel.class);
                  if (!config.containsKey(eventModel.getType())) {
                    logger.error("不能识别的事件");
                    continue;
                  }

                  for (EventHandler handler : config.get(eventModel.getType())) {
                    handler.doHandle(eventModel);
                  }
                }
              }
            });

    thread.start();
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }
}
