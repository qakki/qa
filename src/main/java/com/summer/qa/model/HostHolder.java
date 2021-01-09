package com.summer.qa.model;

import org.springframework.stereotype.Component;

/**
 * @author ：lightingSummer
 * @date ：2019/6/4 0004
 * @description：
 */
@Component
public class HostHolder {
  private static ThreadLocal<User> users = new ThreadLocal<User>();

  public User getUser() {
    return users.get();
  }

  public void setUser(User user) {
    users.set(user);
  }

  public void clear() {
    users.remove();
  }
}
