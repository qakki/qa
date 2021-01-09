package com.summer.qa.service.Impl;

import com.summer.qa.dao.TicketMapper;
import com.summer.qa.dao.UserMapper;
import com.summer.qa.model.Ticket;
import com.summer.qa.model.User;
import com.summer.qa.service.UserService;
import com.summer.qa.util.QAUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ：lightingSummer
 * @date ：2019/6/3 0003
 * @description：
 */
@Service
public class UserServiceImpl implements UserService {

  // 用户名合法性正则：只能含有数字、汉字、字母、下划线
  private static final String USER_NAME_LEGAL_REGEX = "^[\\dA-Za-z_\\u4e00-\\u9fa5]+$";
  // 用户密码合法性正则：只能含有数字、字母、下划线和特殊字符
  private static final String USER_PASSWORD_LEGAL_REGEX = "^[\\dA-Za-z_._~!@#$^&*]+$";
  // 用户密码弱强度正则：纯数字或者纯字母
  private static final String USER_PASSWORD_STRONG_REGEX = "^(\\d*|[a-zA-Z]*)$";

  @Autowired private UserMapper userMapper;
  @Autowired private TicketMapper ticketMapper;

  /**
   * @author: lightingSummer
   * @date: 2019/6/3 0003
   * @description: 根据id获取用户信息
   */
  @Override
  public User getUserById(int id) {
    return userMapper.selectByPrimaryKey(id);
  }

  /**
   * @author: lightingSummer
   * @date: 2019/6/4 0004
   * @description: 用户注册
   */
  @Override
  public Map<String, Object> register(String name, String password) {
    Map<String, Object> map = new HashMap<>();
    if (StringUtils.isBlank(name)) {
      map.put("msg", "用户名不能为空");
      return map;
    }
    if (name.length() > 10) {
      map.put("msg", "用户名过长");
      return map;
    }
    // 判断用户名是否包含非法字符
    Pattern namePattern = Pattern.compile(USER_NAME_LEGAL_REGEX);
    Matcher nameMatcher = namePattern.matcher(name);
    if (!nameMatcher.matches()) {
      map.put("msg", "用户名存在非法字符");
      return map;
    }
    // 密码长度检测
    if (StringUtils.isBlank(password)) {
      map.put("msg", "用户密码不能为空");
      return map;
    } else if (password.length() < 6) {
      map.put("msg", "用户密码过短");
      return map;
    } else if (password.length() > 20) {
      map.put("msg", "用户密码过长");
      return map;
    }
    // 判断密码是否包含非法字符
    Pattern psdPattern = Pattern.compile(USER_PASSWORD_LEGAL_REGEX);
    Matcher psdMatcher = psdPattern.matcher(password);
    if (!psdMatcher.matches()) {
      map.put("msg", "用户密码存在非法字符");
      return map;
    }
    // 密码强度检测
    Pattern psdStrongPattern = Pattern.compile(USER_PASSWORD_STRONG_REGEX);
    Matcher psdStrongMatcher = psdStrongPattern.matcher(password);
    if (psdStrongMatcher.matches()) {
      map.put("msg", "用户密码弱爆了");
      return map;
    }
    User userIfEmpty = userMapper.selectByName(name);
    if (userIfEmpty != null) {
      map.put("msg", "用户名已经被注册");
      return map;
    }
    // 可以正常注册
    User user = new User();
    String salt = UUID.randomUUID().toString().substring(0, 5);
    String head =
        String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000));
    user.setName(name);
    user.setSalt(salt);
    user.setPassword(QAUtil.MD5(password + salt));
    user.setHeadUrl(head);
    userMapper.insertSelective(user);
    // 获取用户id
    User temp = userMapper.selectByName(name);
    String ticket = addUserLoginTicket(temp.getId());
    map.put("ticket", ticket);
    return map;
  }

  /**
   * @author: lightingSummer
   * @date: 2019/6/4 0004
   * @description: 用户登录
   */
  @Override
  public Map<String, Object> login(String name, String password) {
    Map<String, Object> map = new HashMap<>();
    if (StringUtils.isBlank(name)) {
      map.put("msg", "用户名不能为空");
      return map;
    }
    if (StringUtils.isBlank(password)) {
      map.put("msg", "用户密码不能为空");
      return map;
    }
    User userIfEmpty = userMapper.selectByName(name);
    if (userIfEmpty == null) {
      map.put("msg", "用户名不存在");
      return map;
    }
    if (!userIfEmpty.getPassword().equals(QAUtil.MD5(password + userIfEmpty.getSalt()))) {
      map.put("msg", "密码不正确");
      return map;
    }
    // 正常登录
    String ticket = addUserLoginTicket(userIfEmpty.getId());
    map.put("ticket", ticket);
    return map;
  }

  /**
   * @author: lightingSummer
   * @date: 2019/6/4 0004
   * @description: 用户退出
   */
  @Override
  public void logout(String ticket) {
    ticketMapper.updateStatusByTicket(ticket, 1);
  }

  /**
   * @author: lightingSummer
   * @date: 2019/6/6 0006
   * @description: 通过name获取用户
   */
  @Override
  public User getUserByName(String name) {
    return userMapper.selectByName(name);
  }

  /**
   * @author: lightingSummer
   * @date: 2019/6/4 0004
   * @description: 增加用户登录ticket
   */
  private String addUserLoginTicket(int userId) {
    Ticket ticket = new Ticket();
    ticket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
    ticket.setUserId(userId);
    ticket.setStatus(0);
    Date date = new Date();
    date.setTime(date.getTime() + 1000 * 3600 * 24);
    ticket.setExpired(date);
    ticketMapper.insertSelective(ticket);
    return ticket.getTicket();
  }
}
