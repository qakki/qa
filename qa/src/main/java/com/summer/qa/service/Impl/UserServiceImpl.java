package com.summer.qa.service.Impl;

import com.summer.qa.dao.UserMapper;
import com.summer.qa.model.User;
import com.summer.qa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ：lightingSummer
 * @date ：2019/6/3 0003
 * @description：
 */
@Service
public class UserServiceImpl implements UserService {

  @Autowired private UserMapper userMapper;

  /**
   * @author: lightingSummer
   * @date: 2019/6/3 0003
   * @description: 根据id获取用户信息
   */
  @Override
  public User getUserById(int id) {
    return userMapper.selectByPrimaryKey(id);
  }
}
