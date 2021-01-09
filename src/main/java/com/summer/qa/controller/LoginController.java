package com.summer.qa.controller;

import com.summer.qa.service.UserService;
import com.summer.qa.util.SettingUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author ：lightingSummer
 * @date ：2019/6/3 0003
 * @description：
 */
@Controller
public class LoginController {
  private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

  @Autowired private UserService userService;

  /**
   * @author: lightingSummer
   * @date: 2019/6/4 0004
   * @description: 用户登录页面
   */
  @RequestMapping(
      path = {"/reglogin"},
      method = {RequestMethod.GET})
  public String regloginPage(
      Model model, @RequestParam(value = "next", required = false) String next) {
    model.addAttribute("next", next);
    return "login";
  }

  /**
   * @author: lightingSummer
   * @date: 2019/6/4 0004
   * @description: 用户登录
   */
  @RequestMapping(
      path = {"/login/"},
      method = {RequestMethod.POST})
  public String login(
      Model model,
      @RequestParam("username") String username,
      @RequestParam("password") String password,
      @RequestParam(value = "next", required = false) String next,
      @RequestParam(value = "rememberme", defaultValue = "false") boolean rememberme,
      HttpServletResponse response) {
    try {
      Map<String, Object> map = userService.login(username, password);
      if (map.containsKey("ticket")) {
        Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
        if (rememberme) {
          cookie.setMaxAge(3600 * 24 * 5);
        }
        cookie.setPath("/");
        response.addCookie(cookie);
        if (StringUtils.isNotBlank(next)) {
          return "redirect:" + next;
        } else {
          return "redirect:" + SettingUtil.QA_DOMAIN;
        }
      } else {
        model.addAttribute("msg", map.get("msg"));
        return "login";
      }
    } catch (Exception e) {
      logger.error(" LOGIN ERROR " + e.getMessage());
      model.addAttribute("msg", "服务器异常");
      return "login";
    }
  }

  /**
   * @author: lightingSummer
   * @date: 2019/6/4 0004
   * @description: 用户注册
   */
  @RequestMapping(
      path = {"/reg/"},
      method = {RequestMethod.POST})
  public String reg(
      Model model,
      @RequestParam("username") String username,
      @RequestParam("password") String password,
      @RequestParam("next") String next,
      @RequestParam(value = "rememberme", defaultValue = "false") boolean rememberme,
      HttpServletResponse response) {
    try {
      Map<String, Object> map = userService.register(username, password);
      if (map.containsKey("ticket")) {
        Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
        if (rememberme) {
          cookie.setMaxAge(3600 * 24 * 5);
        }
        cookie.setPath("/");
        response.addCookie(cookie);
        if (StringUtils.isNotBlank(next)) {
          return "redirect:" + next;
        } else {
          return "redirect:" + SettingUtil.QA_DOMAIN;
        }
      } else {
        model.addAttribute("msg", map.get("msg"));
        return "login";
      }
    } catch (Exception e) {
      logger.error(" REGISTER ERROR " + e.getMessage());
      model.addAttribute("msg", "服务器异常");
      return "login";
    }
  }

  /**
   * @author: lightingSummer
   * @date: 2019/6/4 0004
   * @description: 用户登出
   */
  @RequestMapping(
      value = "/logout",
      method = {RequestMethod.GET, RequestMethod.POST})
  public String logout(@CookieValue("ticket") String ticket) {
    userService.logout(ticket);
    return "redirect:" + SettingUtil.QA_DOMAIN;
  }
}
