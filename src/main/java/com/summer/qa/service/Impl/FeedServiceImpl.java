package com.summer.qa.service.Impl;

import com.summer.qa.dao.FeedMapper;
import com.summer.qa.model.Feed;
import com.summer.qa.service.FeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ：lightingSummer
 * @date ：2019/6/15 0015
 * @description：
 */
@Service
public class FeedServiceImpl implements FeedService {

  @Autowired private FeedMapper feedMapper;

  @Override
  public List<Feed> getUserFeeds(int maxId, List<Integer> userIds, int count) {
    return feedMapper.selectUserFeeds(maxId, userIds, count);
  }

  @Override
  public boolean addFeed(Feed feed) {
    feedMapper.insertSelective(feed);
    return feed.getId() > 0;
  }

  @Override
  public Feed getFeedById(int id) {
    return feedMapper.selectByPrimaryKey(id);
  }
}
