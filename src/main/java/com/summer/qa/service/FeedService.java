package com.summer.qa.service;

import com.summer.qa.model.Feed;

import java.util.List;

/**
 * @author ：lightingSummer
 * @date ：2019/6/15 0015
 * @description：
 */
public interface FeedService {

  List<Feed> getUserFeeds(int maxId, List<Integer> userIds, int count);

  boolean addFeed(Feed feed);

  Feed getFeedById(int id);
}
