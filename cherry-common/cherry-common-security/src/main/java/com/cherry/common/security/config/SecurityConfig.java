package com.cherry.common.security.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import com.cherry.common.security.config.properties.SecurityProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 权限安全配置
 *
 * @author keer
 * @date 2025-05-29
 */
@Slf4j
@AutoConfiguration
@EnableConfigurationProperties(SecurityProperties.class)
@RequiredArgsConstructor
public class SecurityConfig implements WebMvcConfigurer {
  // todo

  /** 注册sa-token的拦截器 */
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    // 注册 Sa-Token 拦截器，打开注解式鉴权功能
    // registry.addInterceptor(new SaInterceptor()).addPathPatterns("/**");
    registry
        .addInterceptor(
            new SaInterceptor(
                handler -> {
                  SaRouter.match("/**").check(r -> StpUtil.checkLogin());
                }))
        .addPathPatterns("/**");
  }
}
