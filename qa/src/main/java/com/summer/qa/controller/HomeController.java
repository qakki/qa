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
import org.springframework.web.bind.annotation.RequestParam;

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
    model.addAttribute("vos", getQuestions(userId, 0, 20));

    User user = userService.getUserById(userId);
    ViewObject vo = new ViewObject();
    vo.set("user", user);
    vo.set("commentCount", commentService.getUserCommentCount(userId));
    vo.set("followerCount", followService.getFollowerCount(SettingUtil.ENTITY_USER, userId));
    vo.set("followeeCount", followService.getFolloweeCount(userId, SettingUtil.ENTITY_USER));
    if (hostHolder.getUser() != null) {
      if (hostHolder.getUser().getId() != userId) {
        model.addAttribute("couldFollow", 1);
      }
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
  public String index(Model model, @RequestParam(value = "page", defaultValue = "1") int page) {
    int count = (int) Math.floor(questionService.getQuestionCount(0) / 10.0);
    if (page <= 0 && page > count) {
      // 回首页
      model.addAttribute("vos", getQuestions(0, 0, 10));
      model.addAttribute("nowPage", 1);
    } else {
      model.addAttribute("vos", getQuestions(0, page, 10));
      model.addAttribute("nowPage", page);
    }
    model.addAttribute("domain", SettingUtil.QA_DOMAIN);
    model.addAttribute("count", count);
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
      if (question.getContent().length() > 300) {
        question.setContent(question.getContent().substring(0, 300) + "...");
      }
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
