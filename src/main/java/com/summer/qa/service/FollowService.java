package com.summer.qa.service;

import java.util.List;

/**
 * @author ：lightingSummer
 * @date ：2019/6/12 0012
 * @description：
 */
public interface FollowService {

  boolean follow(int userId, int entityType, int entityId);

  boolean unFollow(int userId, int entityType, int entityId);

  List<Integer> getFollowers(int entityType, int entityId, int count);

  List<Integer> getFollowers(int entityType, int entityId, int offset, int count);

  List<Integer> getFollowees(int userId, int entityType, int count);

  List<Integer> getFollowees(int userId, int entityType, int offset, int count);

  long getFollowerCount(int entityType, int entityId);

  long getFolloweeCount(int userId, int entityType);

  boolean isFollower(int userId, int entityType, int entityId);
}
