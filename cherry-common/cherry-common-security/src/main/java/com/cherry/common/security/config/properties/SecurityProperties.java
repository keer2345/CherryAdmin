package com.cherry.common.security.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Security 配置属性
 *
 * @author keer
 * @date 2025-05-29
 */
@Data
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {
  /** 排除路径 */
  private String[] excludes;
}
