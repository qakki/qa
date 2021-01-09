package com.summer.qa.service;

import com.summer.qa.model.User;

import java.util.Map;

/**
 * @author ：lightingSummer
 * @date ：2019/6/3 0003
 * @description：
 */
public interface UserService {

  User getUserById(int id);

  Map<String, Object> register(String username, String password);

  Map<String, Object> login(String name, String password);

  void logout(String ticket);

  User getUserByName(String name);
}
