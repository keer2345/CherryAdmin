package com.cherry.common.web.config;

import com.cherry.common.web.handler.GlobalExceptionHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 通用配置
 *
 * @author keer
 * @date 2025-05-23
 */
@AutoConfiguration
public class ResourcesConfig implements WebMvcConfigurer {
  // todo

  /** 全局异常处理器 */
  @Bean
  public GlobalExceptionHandler globalExceptionHandler() {
    return new GlobalExceptionHandler();
  }
}
