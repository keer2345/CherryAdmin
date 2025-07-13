package com.cherry.common.security.config;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.filter.SaTokenContextFilterForJakartaServlet;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import com.cherry.common.core.exception.SseException;
import com.cherry.common.core.utils.ServletUtils;
import com.cherry.common.core.utils.SpringUtils;
import com.cherry.common.core.utils.StringUtils;
import com.cherry.common.satoken.utils.LoginHelper;
import com.cherry.common.security.config.properties.SecurityProperties;
import com.cherry.common.security.handler.AllUrlHandler;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.DispatcherType;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.EnumSet;

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

  private final SecurityProperties securityProperties;

  /** 注册sa-token的拦截器 */
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    // 注册路由拦截器，自定义验证规则
    registry
        .addInterceptor(
            new SaInterceptor(
                handler -> {
                  AllUrlHandler allUrlHandler = SpringUtils.getBean(AllUrlHandler.class);
                  // 登录验证 -- 排除多个路径
                  SaRouter
                      // 获取所有的
                      .match(allUrlHandler.getUrls())
                      // 对未排除的路径进行检查
                      .check(
                          () -> {
                            HttpServletRequest request = ServletUtils.getRequest();
                            // 检查是否登录 是否有token
                            try {
                              StpUtil.checkLogin();
                            } catch (NotLoginException e) {
                              if (request.getRequestURI().contains("sse")) {
                                throw new SseException(e.getMessage(), e.getCode());
                              } else {
                                throw e;
                              }
                            }

                            // 检查 header 与 param 里的 clientid 与 token 里的是否一致
                            String headerCid = request.getHeader(LoginHelper.CLIENT_KEY);
                            String paramCid = ServletUtils.getParameter(LoginHelper.CLIENT_KEY);
                            String clientId = StpUtil.getExtra(LoginHelper.CLIENT_KEY).toString();
                            if (!StringUtils.equalsAny(clientId, headerCid, paramCid)) {
                              // token 无效
                              throw NotLoginException.newInstance(
                                  StpUtil.getLoginType(),
                                  "-100",
                                  "客户端ID与Token不匹配",
                                  StpUtil.getTokenValue());
                            }

                            // 有效率影响 用于临时测试
                            // if (log.isDebugEnabled()) {
                            //     log.info("剩余有效时间: {}", StpUtil.getTokenTimeout());
                            //     log.info("临时有效时间: {}", StpUtil.getTokenActivityTimeout());
                            // }

                          });
                }))
        .addPathPatterns("/**")
        .excludePathPatterns("/error")
        // 排除不需要拦截的路径
        .excludePathPatterns(securityProperties.getExcludes());
  }

  // todo
  // todo 2025053001

    // todo
  /**
   * https://github.com/QingQiuGeek/blog-backend/blob/e1df553/src/main/java/com/serein/interceptor/SaTokenConfigure.java
   *
   * <p>解决SaTokenContext 上下文尚未初始化的问题
   *
   * <p>参考: https://gitee.com/dromara/sa-token/issues/IC4XFE
   *
   * @return
   */
  /*
  @Bean
  public FilterRegistrationBean saTokenContextFilterForJakartaServlet() {
    FilterRegistrationBean bean =
        new FilterRegistrationBean<>(new SaTokenContextFilterForJakartaServlet());
    // 配置 Filter 拦截的 URL 模式
    bean.addUrlPatterns("/*");
    // 设置 Filter 的执行顺序,数值越小越先执行
    bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
    bean.setAsyncSupported(true);
    bean.setDispatcherTypes(EnumSet.of(DispatcherType.ASYNC, DispatcherType.REQUEST));
    return bean;
  }

   */
}
