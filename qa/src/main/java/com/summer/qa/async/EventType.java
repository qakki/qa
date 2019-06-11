package com.summer.qa.async;

/**
 * @author ：lightingSummer
 * @date ：2019/6/11 0011
 * @description：
 */
public enum EventType {
  LIKE_EVENT(0),
  COMMENT_EVENT(1);

  private int value;

  EventType(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}
