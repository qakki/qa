package com.summer.qa.controller;

import com.summer.qa.async.EventProducer;
import com.summer.qa.model.HostHolder;
import com.summer.qa.model.Question;
import com.summer.qa.model.User;
import com.summer.qa.model.ViewObject;
import com.summer.qa.service.CommentService;
import com.summer.qa.service.FollowService;
import com.summer.qa.service.QuestionService;
import com.summer.qa.service.UserService;
import com.summer.qa.util.QAUtil;
import com.summer.qa.util.SettingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class FollowController {
  @Autowired FollowService followService;

  @Autowired CommentService commentService;

  @Autowired QuestionService questionService;

  @Autowired UserService userService;

  @Autowired HostHolder hostHolder;

  @Autowired EventProducer eventProducer;

  @RequestMapping(
      path = {"/follow/followUser"},
      method = {RequestMethod.POST, RequestMethod.GET})
  @ResponseBody
  public String followUser(@RequestParam("userId") int userId) {
    if (hostHolder.getUser() == null) {
      return QAUtil.getJSONString(999);
    } else if (hostHolder.getUser().getId() == userId) {
      return QAUtil.getJSONString(1, "不能关注自己");
    }

    boolean ret =
        followService.follow(hostHolder.getUser().getId(), SettingUtil.ENTITY_USER, userId);

    // 返回关注的人数
    return QAUtil.getJSONString(
        ret ? 0 : 1,
        String.valueOf(
            followService.getFolloweeCount(hostHolder.getUser().getId(), SettingUtil.ENTITY_USER)));
  }

  @RequestMapping(
      path = {"/follow/unfollowUser"},
      method = {RequestMethod.POST})
  @ResponseBody
  public String unfollowUser(@RequestParam("userId") int userId) {
    if (hostHolder.getUser() == null) {
      return QAUtil.getJSONString(999);
    }

    boolean ret =
        followService.unFollow(hostHolder.getUser().getId(), SettingUtil.ENTITY_USER, userId);

    // 返回关注的人数
    return QAUtil.getJSONString(
        ret ? 0 : 1,
        String.valueOf(
            followService.getFolloweeCount(hostHolder.getUser().getId(), SettingUtil.ENTITY_USER)));
  }

  @RequestMapping(
      path = {"/follow/followQuestion"},
      method = {RequestMethod.POST})
  @ResponseBody
  public String followQuestion(@RequestParam("questionId") int questionId) {
    if (hostHolder.getUser() == null) {
      return QAUtil.getJSONString(999);
    }

    Question q = questionService.getQuestionById(questionId);
    if (q == null) {
      return QAUtil.getJSONString(1, "问题不存在");
    }

    boolean ret =
        followService.follow(hostHolder.getUser().getId(), SettingUtil.ENTITY_QUESTION, questionId);

    Map<String, Object> info = new HashMap<>();
    info.put("headUrl", hostHolder.getUser().getHeadUrl());
    info.put("name", hostHolder.getUser().getName());
    info.put("id", hostHolder.getUser().getId());
    info.put("count", followService.getFollowerCount(SettingUtil.ENTITY_QUESTION, questionId));
    return QAUtil.getJSONString(ret ? 0 : 1, info);
  }

  @RequestMapping(
      path = {"/follow/unfollowQuestion"},
      method = {RequestMethod.POST})
  @ResponseBody
  public String unfollowQuestion(@RequestParam("questionId") int questionId) {
    if (hostHolder.getUser() == null) {
      return QAUtil.getJSONString(999);
    }

    Question q = questionService.getQuestionById(questionId);
    if (q == null) {
      return QAUtil.getJSONString(1, "问题不存在");
    }

    boolean ret =
        followService.unFollow(
            hostHolder.getUser().getId(), SettingUtil.ENTITY_QUESTION, questionId);

    Map<String, Object> info = new HashMap<>();
    info.put("id", hostHolder.getUser().getId());
    info.put("count", followService.getFollowerCount(SettingUtil.ENTITY_QUESTION, questionId));
    return QAUtil.getJSONString(ret ? 0 : 1, info);
  }

  @RequestMapping(
      path = {"/user/{uid}/followers"},
      method = {RequestMethod.GET})
  public String followers(Model model, @PathVariable("uid") int userId) {
    List<Integer> followerIds = followService.getFollowers(SettingUtil.ENTITY_USER, userId, 0, 10);
    if (hostHolder.getUser() != null) {
      model.addAttribute("followers", getUsersInfo(hostHolder.getUser().getId(), followerIds));
    } else {
      model.addAttribute("followers", getUsersInfo(0, followerIds));
    }
    model.addAttribute(
        "followerCount", followService.getFollowerCount(SettingUtil.ENTITY_USER, userId));
    model.addAttribute("curUser", userService.getUserById(userId));
    return "followers";
  }

  @RequestMapping(
      path = {"/user/{uid}/followees"},
      method = {RequestMethod.GET})
  public String followees(Model model, @PathVariable("uid") int userId) {
    List<Integer> followeeIds = followService.getFollowees(userId, SettingUtil.ENTITY_USER, 0, 10);

    if (hostHolder.getUser() != null) {
      model.addAttribute("followees", getUsersInfo(hostHolder.getUser().getId(), followeeIds));
    } else {
      model.addAttribute("followees", getUsersInfo(0, followeeIds));
    }
    model.addAttribute(
        "followeeCount", followService.getFolloweeCount(userId, SettingUtil.ENTITY_USER));
    model.addAttribute("curUser", userService.getUserById(userId));
    return "followees";
  }

  private List<ViewObject> getUsersInfo(int localUserId, List<Integer> userIds) {
    List<ViewObject> userInfos = new ArrayList<ViewObject>();
    for (Integer uid : userIds) {
      User user = userService.getUserById(uid);
      if (user == null) {
        continue;
      }
      ViewObject vo = new ViewObject();
      vo.set("user", user);
      vo.set("commentCount", commentService.getUserCommentCount(uid));
      vo.set("followerCount", followService.getFollowerCount(SettingUtil.ENTITY_USER, uid));
      vo.set("followeeCount", followService.getFolloweeCount(uid, SettingUtil.ENTITY_USER));
      if (localUserId != 0) {
        vo.set("followed", followService.isFollower(localUserId, SettingUtil.ENTITY_USER, uid));
      } else {
        vo.set("followed", false);
      }
      userInfos.add(vo);
    }
    return userInfos;
  }
}
