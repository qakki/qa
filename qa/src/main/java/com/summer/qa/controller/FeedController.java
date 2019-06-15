package com.summer.qa.controller;

import com.summer.qa.model.Feed;
import com.summer.qa.model.HostHolder;
import com.summer.qa.service.FeedService;
import com.summer.qa.service.FollowService;
import com.summer.qa.util.JedisAdapter;
import com.summer.qa.util.RedisKeyUtil;
import com.summer.qa.util.SettingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ：lightingSummer
 * @date ：2019/6/15 0015
 * @description：
 */
@Controller
public class FeedController {
  private static final Logger logger = LoggerFactory.getLogger(FeedController.class);

  @Autowired private FeedService feedService;

  @Autowired private FollowService followService;

  @Autowired private HostHolder hostHolder;

  @Autowired private JedisAdapter jedisAdapter;

  @RequestMapping(
      path = {"/pullfeeds"},
      method = {RequestMethod.GET, RequestMethod.POST})
  public String getPullFeeds(Model model) {
    int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
    List<Integer> followees = new ArrayList<>();
    if (localUserId != 0) {
      // 关注的人
      followees =
          followService.getFollowees(localUserId, SettingUtil.ENTITY_USER, Integer.MAX_VALUE);
    }
    List<Feed> feeds = feedService.getUserFeeds(Integer.MAX_VALUE, followees, 10);
    model.addAttribute("feeds", feeds);
    return "feeds";
  }

  @RequestMapping(
      path = {"/pushfeeds"},
      method = {RequestMethod.GET, RequestMethod.POST})
  public String getPushFeeds(Model model) {
    int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
    List<String> feedIds = jedisAdapter.lrange(RedisKeyUtil.getTimelineKey(localUserId), 0, 10);
    List<Feed> feeds = new ArrayList<Feed>();
    for (String feedId : feedIds) {
      Feed feed = feedService.getFeedById(Integer.parseInt(feedId));
      if (feed != null) {
        feeds.add(feed);
      }
    }
    model.addAttribute("feeds", feeds);
    return "feeds";
  }
}
