package com.summer.qa.service.Impl;

import com.summer.qa.service.FollowService;
import com.summer.qa.util.JedisAdapter;
import com.summer.qa.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author ：lightingSummer
 * @date ：2019/6/12 0012
 * @description：
 */
@Service
public class FollowServiceImpl implements FollowService {

  @Autowired private JedisAdapter jedisAdapter;

  /**
   * @author: lightingSummer
   * @date: 2019/6/12 0012
   * @description: 关注
   */
  public boolean follow(int userId, int entityType, int entityId) {
    String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
    String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
    Date date = new Date();
    // 实体的粉丝增加当前用户
    Jedis jedis = jedisAdapter.getJedis();
    Transaction tx = jedisAdapter.multi(jedis);
    tx.zadd(followerKey, date.getTime(), String.valueOf(userId));
    // 当前用户对这类实体关注+1
    tx.zadd(followeeKey, date.getTime(), String.valueOf(entityId));
    List<Object> ret = jedisAdapter.exec(tx, jedis);
    return ret.size() == 2 && (Long) ret.get(0) > 0 && (Long) ret.get(1) > 0;
  }

  /**
   * @author: lightingSummer
   * @date: 2019/6/12 0012
   * @description: 取消关注
   */
  public boolean unFollow(int userId, int entityType, int entityId) {
    String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
    String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
    // 实体的粉丝删除当前用户
    Jedis jedis = jedisAdapter.getJedis();
    Transaction tx = jedisAdapter.multi(jedis);
    tx.zrem(followerKey, String.valueOf(userId));
    // 当前用户对这类实体关注-1
    tx.zrem(followeeKey, String.valueOf(entityId));
    List<Object> ret = jedisAdapter.exec(tx, jedis);
    return ret.size() == 2 && (Long) ret.get(0) > 0 && (Long) ret.get(1) > 0;
  }

  public List<Integer> getFollowers(int entityType, int entityId, int count) {
    String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
    return getIdsFromSet(jedisAdapter.zrevrange(followerKey, 0, count));
  }

  public List<Integer> getFollowers(int entityType, int entityId, int offset, int count) {
    String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
    return getIdsFromSet(jedisAdapter.zrevrange(followerKey, offset, offset + count));
  }

  public List<Integer> getFollowees(int userId, int entityType, int count) {
    String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
    return getIdsFromSet(jedisAdapter.zrevrange(followeeKey, 0, count));
  }

  public List<Integer> getFollowees(int userId, int entityType, int offset, int count) {
    String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
    return getIdsFromSet(jedisAdapter.zrevrange(followeeKey, offset, offset + count));
  }

  public long getFollowerCount(int entityType, int entityId) {
    String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
    return jedisAdapter.zcard(followerKey);
  }

  public long getFolloweeCount(int userId, int entityType) {
    String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
    return jedisAdapter.zcard(followeeKey);
  }

  public boolean isFollower(int userId, int entityType, int entityId) {
    String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
    return jedisAdapter.zscore(followerKey, String.valueOf(userId)) != null;
  }

  private List<Integer> getIdsFromSet(Set<String> idset) {
    List<Integer> ids = new ArrayList<>();
    for (String str : idset) {
      ids.add(Integer.parseInt(str));
    }
    return ids;
  }
}
