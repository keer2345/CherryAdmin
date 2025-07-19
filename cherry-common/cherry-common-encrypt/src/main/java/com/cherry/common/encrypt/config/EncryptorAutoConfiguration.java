package com.cherry.common.encrypt.config;

import com.cherry.common.encrypt.core.EncryptorManager;
import com.cherry.common.encrypt.interceptor.MybatisDecryptInterceptor;
import com.cherry.common.encrypt.interceptor.MybatisEncryptInterceptor;
import com.cherry.common.encrypt.properties.EncryptorProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 加解密配置
 *
 * @author 老马
 * @version 4.6.0
 */
@AutoConfiguration
@EnableConfigurationProperties(EncryptorProperties.class)
@ConditionalOnProperty(value = "mybatis-encryptor.enable", havingValue = "true")
@Slf4j
public class EncryptorAutoConfiguration {

  @Autowired private EncryptorProperties properties;

  @Bean
  public EncryptorManager encryptorManager() {
    return new EncryptorManager();
  }

  @Bean
  public MybatisEncryptInterceptor mybatisEncryptInterceptor(EncryptorManager encryptorManager) {
    return new MybatisEncryptInterceptor(encryptorManager, properties);
  }

  @Bean
  public MybatisDecryptInterceptor mybatisDecryptInterceptor(EncryptorManager encryptorManager) {
    return new MybatisDecryptInterceptor(encryptorManager, properties);
  }
}
