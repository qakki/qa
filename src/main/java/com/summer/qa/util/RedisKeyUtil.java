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
  // 获取粉丝
  private static String BIZ_FOLLOWER = "FOLLOWER";
  // 关注对象
  private static String BIZ_FOLLOWEE = "FOLLOWEE";
  private static String BIZ_TIMELINE = "QA_TIMELINE";

  public static String getEventQueueKey() {
    return BIZ_EVENT;
  }

  public static String getLikeKey(int entityType, int entityId) {
    return BIZ_DOMAIN + BIZ_LIKE + SPLIT + entityType + SPLIT + entityId;
  }

  public static String getDislikeKey(int entityType, int entityId) {
    return BIZ_DOMAIN + BIZ_DISLIKE + SPLIT + entityType + SPLIT + entityId;
  }

  // 某个实体的粉丝key
  public static String getFollowerKey(int entityType, int entityId) {
    return BIZ_FOLLOWER + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
  }

  // 每个用户对某类实体的关注key
  public static String getFolloweeKey(int userId, int entityType) {
    return BIZ_FOLLOWEE + SPLIT + String.valueOf(userId) + SPLIT + String.valueOf(entityType);
  }

  public static String getTimelineKey(int userId) {
    return BIZ_TIMELINE + SPLIT + String.valueOf(userId);
  }
}
