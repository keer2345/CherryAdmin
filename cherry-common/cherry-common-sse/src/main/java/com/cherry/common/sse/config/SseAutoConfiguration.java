package com.cherry.common.sse.config;

import com.cherry.common.sse.controller.SseController;
import com.cherry.common.sse.core.SseEmitterManager;
import com.cherry.common.sse.listener.SseTopicListener;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * SSE 自动装配
 *
 * @author keer
 * @date 2025-05-30
 */
@AutoConfiguration
@ConditionalOnProperty(value = "sse.enabled", havingValue = "true")
@EnableConfigurationProperties(SseProperties.class)
public class SseAutoConfiguration {
  @Bean
  public SseEmitterManager sseEmitterManager() {
    return new SseEmitterManager();
  }

  @Bean
  public SseTopicListener sseTopicListener() {
    return new SseTopicListener();
  }

  @Bean
  public SseController sseController(SseEmitterManager sseEmitterManager) {
    return new SseController(sseEmitterManager);
  }
}
