package com.cherry.common.flex.config;

import com.cherry.common.flex.handler.MyLogicDeleteProcessor;
import com.cherry.common.flex.utils.UUIDKeyGenerator;
import com.mybatisflex.core.keygen.KeyGeneratorFactory;
import com.mybatisflex.core.logicdelete.LogicDeleteProcessor;
import com.mybatisflex.core.mybatis.FlexConfiguration;
import com.mybatisflex.spring.boot.ConfigurationCustomizer;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatisFlexConfiguration
 *
 * @author keer2345
 * @date 2025-06-23
 */
@Configuration
public class MyConfigurationCustomizer implements ConfigurationCustomizer {
  // todo

  @Override
  public void customize(FlexConfiguration configuration) {
    // 输出 sql 日志
    configuration.setLogImpl(StdOutImpl.class);
    // 主键策略
    KeyGeneratorFactory.register("uuid", new UUIDKeyGenerator());
  }

  @Bean
  public LogicDeleteProcessor logicDeleteProcessor(){
    return new MyLogicDeleteProcessor();
  }
}
