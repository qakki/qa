package com.summer.qa.configuration;

import com.summer.qa.Interceptor.LoginRequiredInterceptor;
import com.summer.qa.Interceptor.MsgAuthInterceptor;
import com.summer.qa.Interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author ：lightingSummer
 * @date ：2019/6/4 0004
 * @description：
 */
@Component
public class QAWebConfig extends WebMvcConfigurerAdapter {

  @Autowired private PassportInterceptor passportInterceptor;
  @Autowired private LoginRequiredInterceptor loginRequiredInterceptor;
  @Autowired private MsgAuthInterceptor msgAuthInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    // cookie校验
    registry.addInterceptor(passportInterceptor);
    // 需要登录拦截
    registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/addComment");
    registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/pullfeeds");
    registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/pushfeeds");
    registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/follow/*");
    registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/dislike");
    registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/like");
    registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/msg/*");
    registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/setting");
    registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/question/add");
    // 权限校验
    registry.addInterceptor(msgAuthInterceptor).addPathPatterns("/msg/detail");

    super.addInterceptors(registry);
  }
}
