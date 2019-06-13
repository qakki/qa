package com.summer.qa.controller;

import com.summer.qa.model.*;
import com.summer.qa.service.*;
import com.summer.qa.util.QAUtil;
import com.summer.qa.util.SettingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ：lightingSummer
 * @date ：2019/6/5 0005
 * @description：
 */
@Controller
public class QuestionController {
  private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

  @Autowired private QuestionService questionService;
  @Autowired private UserService userService;
  @Autowired private HostHolder hostHolder;
  @Autowired private CommentService commentService;
  @Autowired private LikeService likeService;
  @Autowired private FollowService followService;

  /**
   * @author: lightingSummer
   * @date: 2019/6/4 0006
   * @description: 添加问题
   */
  @RequestMapping(
      path = {"/question/add"},
      method = {RequestMethod.POST})
  @ResponseBody
  public String addQuestion(
      @RequestParam("title") String title, @RequestParam("content") String content) {
    try {
      Question question = new Question();
      if (hostHolder.getUser() == null) {
        return QAUtil.getJSONString(999);
      } else {
        question.setUserId(hostHolder.getUser().getId());
      }
      question.setTitle(title);
      question.setContent(content);
      question.setCreatedDate(new Date());
      if (questionService.addQuestion(question) != 0) {
        return QAUtil.getJSONString(0);
      }
    } catch (Exception e) {
      logger.error("add question failed " + e.getMessage());
    }
    return QAUtil.getJSONString(1, "发布问题失败");
  }

  /**
   * @author: lightingSummer
   * @date: 2019/6/4 0006
   * @description: 问题详情页
   */
  @RequestMapping(
      path = {"/question/{qid}"},
      method = {RequestMethod.GET, RequestMethod.POST})
  public String questionDetail(Model model, @PathVariable int qid) {
    try {
      Question question = questionService.getQuestionById(qid);
      model.addAttribute("question", question);
      model.addAttribute("user", userService.getUserById(question.getUserId()));
      // 查阅评论
      List<ViewObject> vos = new ArrayList<>();
      List<Comment> comments = commentService.getCommentsByEntity(SettingUtil.ENTITY_QUESTION, qid);
      for (Comment comment : comments) {
        ViewObject vo = new ViewObject();
        vo.set("comment", comment);
        if (hostHolder.getUser() == null) {
          vo.set("like", 0);
        } else {
          vo.set(
              "like",
              likeService.getLikeStatus(
                  hostHolder.getUser().getId(), SettingUtil.ENTITY_COMMENT, comment.getId()));
        }
        vo.set("likeCount", likeService.getLikeCount(SettingUtil.ENTITY_COMMENT, comment.getId()));
        vo.set("user", userService.getUserById(comment.getUserId()));
        vos.add(vo);
      }
      model.addAttribute("comments", vos);

      List<ViewObject> followUsers = new ArrayList<ViewObject>();
      // 获取关注的用户信息
      List<Integer> users = followService.getFollowers(SettingUtil.ENTITY_QUESTION, qid, 20);
      for (Integer userId : users) {
        ViewObject vo = new ViewObject();
        User u = userService.getUserById(userId);
        if (u == null) {
          continue;
        }
        vo.set("name", u.getName());
        vo.set("headUrl", u.getHeadUrl());
        vo.set("id", u.getId());
        followUsers.add(vo);
      }
      model.addAttribute("followUsers", followUsers);
      if (hostHolder.getUser() != null) {
        model.addAttribute(
            "followed",
            followService.isFollower(
                hostHolder.getUser().getId(), SettingUtil.ENTITY_QUESTION, qid));
      } else {
        model.addAttribute("followed", false);
      }

    } catch (Exception e) {
      logger.error("get question detail failed " + e.getMessage());
    }
    return "detail";
  }
}
