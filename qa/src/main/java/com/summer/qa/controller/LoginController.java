package com.summer.qa.controller;

import com.summer.qa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @author ：lightingSummer
 * @date ：2019/6/3 0003
 * @description：
 */
@Controller
public class LoginController {

  @Autowired private UserService userService;
}
