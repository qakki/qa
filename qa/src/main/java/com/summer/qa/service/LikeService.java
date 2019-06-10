package com.summer.qa.service;

/**
 * @author ：lightingSummer
 * @date ：2019/6/10 0010
 * @description：
 */
public interface LikeService {
  long getLikeCount(int entityType, int entityId);

  long getLikeStatus(int userId, int entityType, int entityId);

  long like(int userId, int entityType, int entityId);

  long disLike(int userId, int entityType, int entityId);
}
