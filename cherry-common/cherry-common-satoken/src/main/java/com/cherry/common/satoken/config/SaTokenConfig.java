package com.cherry.common.satoken.config;

import cn.dev33.satoken.jwt.StpLogicJwtForSimple;
import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpLogic;
import com.cherry.common.core.factory.YmlPropertySourceFactory;
import com.cherry.common.satoken.core.service.SaPermissionImpl;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

/**
 * sa-token 配置
 *
 * @author keer
 * @date 2025-05-28
 */
@AutoConfiguration
@PropertySource(value = "classpath:common-satoken.yml", factory = YmlPropertySourceFactory.class)
public class SaTokenConfig {
  // todo

  // Sa-Token 整合 jwt (Simple 简单模式)
  @Bean
  public StpLogic getStpLogicJwt() {
    return new StpLogicJwtForSimple();
  }

  /** 权限接口实现(使用bean注入方便用户替换) */
  @Bean
  public StpInterface stpInterface() {
    return new SaPermissionImpl();
  }
}
