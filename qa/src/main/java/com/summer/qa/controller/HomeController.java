package com.summer.qa.controller;

import com.summer.qa.model.Question;
import com.summer.qa.model.User;
import com.summer.qa.model.ViewObject;
import com.summer.qa.service.QuestionService;
import com.summer.qa.service.UserService;
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

  @Autowired private QuestionService questionService;

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
    return "index";
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
      vos.add(vo);
    }
    return vos;
  }
}
