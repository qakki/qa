package com.summer.qa.async;

import java.util.List;

/**
 * @author ：lightingSummer
 * @date ：2019/6/11 0011
 * @description：
 */
public interface EventHandler {
  void doHandle(EventModel model);

  List<EventType> getSupportEventTypes();
}
