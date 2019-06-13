package com.summer.qa.controller;

import com.summer.qa.model.HostHolder;
import com.summer.qa.model.Question;
import com.summer.qa.model.User;
import com.summer.qa.model.ViewObject;
import com.summer.qa.service.CommentService;
import com.summer.qa.service.FollowService;
import com.summer.qa.service.QuestionService;
import com.summer.qa.service.UserService;
import com.summer.qa.util.SettingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ：lightingSummer
 * @date ：2019/6/3 0003
 * @description：
 */
@Controller
public class HomeController {
  private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

  @Autowired private UserService userService;
  @Autowired private CommentService commentService;
  @Autowired private QuestionService questionService;
  @Autowired private FollowService followService;
  @Autowired private HostHolder hostHolder;

  /**
   * @author: lightingSummer
   * @date: 2019/6/4 0006
   * @description: 用户主页
   */
  @RequestMapping(
      value = {"/user/{userId}"},
      method = RequestMethod.GET)
  public String userIndex(Model model, @PathVariable int userId) {
    model.addAttribute("vos", getQuestions(userId, 0, 10));

    User user = userService.getUserById(userId);
    ViewObject vo = new ViewObject();
    vo.set("user", user);
    vo.set("commentCount", commentService.getUserCommentCount(userId));
    vo.set("followerCount", followService.getFollowerCount(SettingUtil.ENTITY_USER, userId));
    vo.set("followeeCount", followService.getFolloweeCount(userId, SettingUtil.ENTITY_USER));
    if (hostHolder.getUser() != null) {
      vo.set(
          "followed",
          followService.isFollower(hostHolder.getUser().getId(), SettingUtil.ENTITY_USER, userId));
    } else {
      vo.set("followed", false);
    }
    model.addAttribute("profileUser", vo);

    return "profile";
  }

  /**
   * @author: lightingSummer
   * @date: 2019/6/4 0006
   * @description: 主页
   */
  @RequestMapping(
      value = {"/", "/index"},
      method = RequestMethod.GET)
  public String index(Model model) {
    model.addAttribute("vos", getQuestions(0, 0, 10));
    return "index";
  }

  /**
   * @author: lightingSummer
   * @date: 2019/6/4 0006
   * @description: 获取问题
   */
  private List<ViewObject> getQuestions(int userId, int page, int limit) {
    List<ViewObject> vos = new ArrayList<>();
    List<Question> questions = questionService.getLatestQuestions(userId, page, limit);
    for (Question question : questions) {
      ViewObject vo = new ViewObject();
      User user = userService.getUserById(question.getUserId());
      vo.set("user", user);
      vo.set("question", question);
      vo.set(
          "followCount",
          followService.getFollowerCount(SettingUtil.ENTITY_QUESTION, question.getId()));
      vos.add(vo);
    }
    return vos;
  }
}
