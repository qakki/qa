package com.summer.qa.Interceptor;

import com.summer.qa.dao.TicketMapper;
import com.summer.qa.dao.UserMapper;
import com.summer.qa.model.HostHolder;
import com.summer.qa.model.Ticket;
import com.summer.qa.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author ：lightingSummer
 * @date ：2019/6/4 0004
 * @description：
 */
@Component
public class PassportInterceptor implements HandlerInterceptor {

  @Autowired private HostHolder hostHolder;

  @Autowired private TicketMapper ticketMapper;

  @Autowired private UserMapper userMapper;

  @Override
  public boolean preHandle(
      HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o)
      throws Exception {
    String ticket = null;
    if (httpServletRequest.getCookies() != null) {
      for (Cookie cookie : httpServletRequest.getCookies()) {
        if (cookie.getName().equals("ticket")) {
          ticket = cookie.getValue();
          break;
        }
      }
    }
    if (ticket != null) {
      Ticket t = ticketMapper.selectByTicket(ticket);
      if (t != null && t.getStatus() == 0 && t.getExpired().after(new Date())) {
        User user = userMapper.selectByPrimaryKey(t.getUserId());
        hostHolder.setUser(user);
      }
    }
    return true;
  }

  @Override
  public void postHandle(
      HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse,
      Object o,
      ModelAndView modelAndView)
      throws Exception {
    if (modelAndView != null && hostHolder.getUser() != null) {
      modelAndView.addObject("user", hostHolder.getUser());
    }
  }

  @Override
  public void afterCompletion(
      HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse,
      Object o,
      Exception e)
      throws Exception {
    hostHolder.clear();
  }
}
