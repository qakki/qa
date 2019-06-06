package com.summer.qa.util;

/**
 * @author ：lightingSummer
 * @date ：2019/5/29 0029
 * @description：
 */
public class RedisKeyUtil {
  private static final String SPLIT = ":";
  private static final String BIZ_LIKE = "LIKE";
  private static final String BIZ_DISLIKE = "DISLIKE";
  private static final String BIZ_EVENT = "QA_EVENT";
  private static final String BIZ_DOMAIN = "QA";

  public static String getEventQueueKey() {
    return BIZ_EVENT;
  }

  public static String getLikeKey(int entityType, int entityId) {
    return BIZ_DOMAIN + BIZ_LIKE + SPLIT + entityType + SPLIT + entityId;
  }

  public static String getDislikeKey(int entityType, int entityId) {
    return BIZ_DOMAIN + BIZ_DISLIKE + SPLIT + entityType + SPLIT + entityId;
  }
}
