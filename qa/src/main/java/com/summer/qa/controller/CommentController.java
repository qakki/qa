package com.summer.qa.controller;

import com.summer.qa.model.Comment;
import com.summer.qa.model.HostHolder;
import com.summer.qa.service.CommentService;
import com.summer.qa.service.QuestionService;
import com.summer.qa.service.SensitiveService;
import com.summer.qa.util.SettingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/**
 * @author ：lightingSummer
 * @date ：2019/6/5 0005
 * @description：
 */
@Controller
public class CommentController {
  private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

  @Autowired private HostHolder hostHolder;
  @Autowired private SensitiveService sensitiveService;
  @Autowired private CommentService commentService;
  @Autowired private QuestionService questionService;

  /**
   * @author: lightingSummer
   * @date: 2019/6/6 0006
   * @description: 添加评论
   */
  @RequestMapping(
      path = {"/addComment"},
      method = {RequestMethod.POST})
  public String addComment(
      @RequestParam("questionId") int questionId, @RequestParam("content") String content) {
    try {
      Comment comment = new Comment();
      // 之后做权限控制之后 在去掉这个匿名
      int userId = hostHolder.getUser() == null ? 1 : hostHolder.getUser().getId();
      comment.setUserId(userId);
      comment.setEntityType(SettingUtil.ENTITY_QUESTION);
      comment.setEntityId(questionId);
      // 敏感词过滤
      comment.setContent(sensitiveService.filter(content));
      comment.setAddTime(new Date());
      if (commentService.addComment(comment) > 0) {
        // 更新question表里的评论数
        int commentCount = commentService.getCommentCount(SettingUtil.ENTITY_QUESTION, questionId);
        questionService.updateCommentCount(questionId, commentCount);
      } else {
        logger.error("add comment failed!");
      }
    } catch (Exception e) {
      logger.error("add comment failed " + e.getMessage());
    }
    return "redirect:" + SettingUtil.QA_DOMAIN + "/question/" + questionId;
  }
}
