package com.summer.qa.configuration;

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

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(passportInterceptor);
    super.addInterceptors(registry);
  }
}
