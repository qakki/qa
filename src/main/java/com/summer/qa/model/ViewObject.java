package com.summer.qa.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ：summerGit
 * @date ：2019/5/20 0020
 * @description：
 */
public class ViewObject {
  private Map<String, Object> objs = new HashMap<String, Object>();

  public void set(String key, Object value) {
    objs.put(key, value);
  }

  public Object get(String key) {
    return objs.get(key);
  }
}
