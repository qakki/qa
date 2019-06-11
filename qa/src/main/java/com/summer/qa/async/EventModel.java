package com.summer.qa.async;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ：lightingSummer
 * @date ：2019/6/11 0011
 * @description：
 */
@Data
public class EventModel {
  private EventType type;
  private int actorId;
  private int entityType;
  private int entityId;
  private int eventOwnerId;
  private Map<String, String> exts = new HashMap<>();

  public String getExts(String key) {
    return exts.get(key);
  }

  public void setExts(String key, String value) {
    exts.put(key, value);
  }
}
