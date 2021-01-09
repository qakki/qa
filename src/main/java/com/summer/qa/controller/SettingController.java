package com.summer.qa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author ：lightingSummer
 * @date ：2019/6/19 0019
 * @description：
 */
@Controller
public class SettingController {
  @RequestMapping(
      path = {"/setting"},
      method = {RequestMethod.GET})
  public String setting(Model model) {
    return "setting";
  }

  @RequestMapping(
      path = {"/noauth"},
      method = {RequestMethod.GET})
  public String auth(Model model) {
    return "auth";
  }
}
