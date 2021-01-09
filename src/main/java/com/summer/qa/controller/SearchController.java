package com.summer.qa.controller;

import com.summer.qa.model.Question;
import com.summer.qa.model.ViewObject;
import com.summer.qa.service.FollowService;
import com.summer.qa.service.QuestionService;
import com.summer.qa.service.SolrService;
import com.summer.qa.service.UserService;
import com.summer.qa.util.SettingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ：lightingSummer
 * @date ：2019/6/21 0021
 * @description：
 */
@Controller
public class SearchController {
  private static final Logger logger = LoggerFactory.getLogger(SearchController.class);
  @Autowired private SolrService searchService;

  @Autowired private FollowService followService;

  @Autowired private UserService userService;

  @Autowired private QuestionService questionService;

  @RequestMapping(
      path = {"/search"},
      method = {RequestMethod.GET})
  public String search(
      Model model,
      @RequestParam("q") String keyword,
      @RequestParam(value = "offset", defaultValue = "0") int offset,
      @RequestParam(value = "count", defaultValue = "10") int count) {
    try {
      List<Question> questionList =
          searchService.searchQuestion(keyword, offset, count, "<font color=\"red\">", "</font>");
      List<ViewObject> vos = new ArrayList<>();
      for (Question question : questionList) {
        Question q = questionService.getQuestionById(question.getId());
        ViewObject vo = new ViewObject();
        if (question.getContent() != null) {
          q.setContent(question.getContent());
        }
        if (question.getTitle() != null) {
          q.setTitle(question.getTitle());
        }
        vo.set("question", q);
        vo.set(
            "followCount",
            followService.getFollowerCount(SettingUtil.ENTITY_QUESTION, question.getId()));
        vo.set("user", userService.getUserById(q.getUserId()));
        vos.add(vo);
      }
      model.addAttribute("vos", vos);
      model.addAttribute("keyword", keyword);
    } catch (Exception e) {
      logger.error("搜索评论失败" + e.getMessage());
    }
    return "result";
  }
}
