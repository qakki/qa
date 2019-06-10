package com.summer.qa.service.Impl;

import com.summer.qa.service.LikeService;
import com.summer.qa.util.JedisAdapter;
import com.summer.qa.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ：lightingSummer
 * @date ：2019/6/10 0010
 * @description：
 */
@Service
public class LikeServiceImpl implements LikeService {

  @Autowired private JedisAdapter jedisAdapter;

  /**
   * @author: lightingSummer
   * @date: 2019/6/10 0010
   * @description: 获取likeCount
   */
  @Override
  public long getLikeCount(int entityType, int entityId) {
    String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
    return jedisAdapter.scard(likeKey);
  }

  /**
   * @author: lightingSummer
   * @date: 2019/6/10 0010
   * @description: 获取喜欢状态
   */
  @Override
  public long getLikeStatus(int userId, int entityType, int entityId) {
    String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
    if (jedisAdapter.sismember(likeKey, String.valueOf(userId))) {
      return 1;
    }
    String disLikeKey = RedisKeyUtil.getDislikeKey(entityType, entityId);
    return jedisAdapter.sismember(disLikeKey, String.valueOf(userId)) ? -1 : 0;
  }

  /**
   * @author: lightingSummer
   * @date: 2019/6/10 0010
   * @description: 点赞
   */
  @Override
  public long like(int userId, int entityType, int entityId) {
    String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
    jedisAdapter.sadd(likeKey, String.valueOf(userId));
    String disLikeKey = RedisKeyUtil.getDislikeKey(entityType, entityId);
    jedisAdapter.srem(disLikeKey, String.valueOf(userId));
    return jedisAdapter.scard(likeKey);
  }

  /**
   * @author: lightingSummer
   * @date: 2019/6/10 0010
   * @description: 点踩
   */
  @Override
  public long disLike(int userId, int entityType, int entityId) {
    String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
    jedisAdapter.srem(likeKey, String.valueOf(userId));
    String disLikeKey = RedisKeyUtil.getDislikeKey(entityType, entityId);
    jedisAdapter.sadd(disLikeKey, String.valueOf(userId));
    return jedisAdapter.scard(likeKey);
  }
}
