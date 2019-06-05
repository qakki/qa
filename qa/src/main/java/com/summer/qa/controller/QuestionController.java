package com.summer.qa.controller;

import com.summer.qa.model.HostHolder;
import com.summer.qa.model.Question;
import com.summer.qa.service.QuestionService;
import com.summer.qa.service.UserService;
import com.summer.qa.util.QAUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

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

  @RequestMapping(
      path = {"/question/{qid}"},
      method = {RequestMethod.GET, RequestMethod.POST})
  public String questionDetail(Model model, @PathVariable int qid) {
    try {
      Question question = questionService.getQuestionById(qid);
      model.addAttribute("question", question);
      model.addAttribute("user", userService.getUserById(question.getUserId()));
    } catch (Exception e) {
      logger.error("get question detail failed " + e.getMessage());
    }
    return "detail";
  }
}
