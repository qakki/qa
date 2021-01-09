package com.summer.qa.async.handler;

import com.alibaba.fastjson.JSONObject;
import com.summer.qa.async.EventHandler;
import com.summer.qa.async.EventModel;
import com.summer.qa.async.EventType;
import com.summer.qa.model.Feed;
import com.summer.qa.model.Question;
import com.summer.qa.model.User;
import com.summer.qa.service.FeedService;
import com.summer.qa.service.FollowService;
import com.summer.qa.service.QuestionService;
import com.summer.qa.service.UserService;
import com.summer.qa.util.JedisAdapter;
import com.summer.qa.util.RedisKeyUtil;
import com.summer.qa.util.SettingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author ：lightingSummer
 * @date ：2019/6/15 0011
 * @description：
 */
@Component
public class FeedHandler implements EventHandler {

  @Autowired private UserService userService;
  @Autowired private QuestionService questionService;
  @Autowired private FeedService feedService;
  @Autowired private FollowService followService;
  @Autowired private JedisAdapter jedisAdapter;

  @Override
  public void doHandle(EventModel model) {
    Feed feed = new Feed();
    feed.setUserId(model.getActorId());
    feed.setCreatedDate(new Date());
    feed.setType(model.getType().getValue());
    feed.setData(buildFeedData(model));
    if (feed.getData() == null) {
      // 不支持的feed
      return;
    }
    feedService.addFeed(feed);

    // 获得所有粉丝
    List<Integer> followers =
        followService.getFollowers(SettingUtil.ENTITY_USER, model.getActorId(), Integer.MAX_VALUE);
    // 系统队列
    followers.add(0);
    // 给所有粉丝推事件
    for (int follower : followers) {
      String timelineKey = RedisKeyUtil.getTimelineKey(follower);
      jedisAdapter.lpush(timelineKey, String.valueOf(feed.getId()));
      // 限制最长长度，如果timelineKey的长度过大，就删除后面的新鲜事
    }
  }

  private String buildFeedData(EventModel model) {
    Map<String, String> map = new HashMap<String, String>();
    // 触发用户是通用的
    User actor = userService.getUserById(model.getActorId());
    if (actor == null) {
      return null;
    }
    map.put("userId", String.valueOf(actor.getId()));
    map.put("userHead", actor.getHeadUrl());
    map.put("userName", actor.getName());

    if (model.getType() == EventType.COMMENT_EVENT) {
      Question question = questionService.getQuestionById(model.getEntityId());
      if (question == null) {
        return null;
      }
      map.put("questionId", String.valueOf(question.getId()));
      map.put("questionTitle", question.getTitle());
      return JSONObject.toJSONString(map);
    }
    return null;
  }

  @Override
  public List<EventType> getSupportEventTypes() {
    return Arrays.asList(EventType.COMMENT_EVENT);
  }
}
