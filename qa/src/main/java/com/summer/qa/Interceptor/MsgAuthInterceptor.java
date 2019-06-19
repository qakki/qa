package com.summer.qa.Interceptor;

import com.summer.qa.model.HostHolder;
import com.summer.qa.util.SettingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ：lightingSummer
 * @date ：2019/6/19 0019
 * @description：
 */
@Component
public class MsgAuthInterceptor implements HandlerInterceptor {

  private static final String regex = "^\\d\\d*_\\d\\d*$";

  @Autowired private HostHolder hostHolder;

  @Override
  public boolean preHandle(
      HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o)
      throws Exception {
    String conversationId = httpServletRequest.getParameter("conversationId");
    Pattern namePattern = Pattern.compile(regex);
    Matcher nameMatcher = namePattern.matcher(conversationId);
    if (!nameMatcher.matches()) {
      return false;
    } else {
      String[] users = conversationId.split("_");
      for (String user : users) {
        if (user.equals(hostHolder.getUser().getId().toString())) {
          return true;
        }
      }
    }
    httpServletResponse.sendRedirect(SettingUtil.QA_DOMAIN + "/noauth");
    return false;
  }

  @Override
  public void postHandle(
      HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse,
      Object o,
      ModelAndView modelAndView)
      throws Exception {}

  @Override
  public void afterCompletion(
      HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse,
      Object o,
      Exception e)
      throws Exception {}
}
