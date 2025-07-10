package com.cherry.system.service.impl;

import com.cherry.common.core.constant.CacheNames;
import com.cherry.common.core.utils.ObjectUtils;
import com.cherry.common.core.utils.StringUtils;
import com.cherry.system.domain.SysConfig;
import com.cherry.system.mapper.SysConfigMapper;
import com.cherry.system.service.ISysConfigService;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 参数配置 服务层实现
 *
 * @author Lion Li
 */
@RequiredArgsConstructor
@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig>
    implements ISysConfigService {
  // todo

  private final SysConfigMapper configMapper;

  /**
   * 根据键名查询参数配置信息
   *
   * @param configKey 参数key
   * @return 参数键值
   */
  @Cacheable(cacheNames = CacheNames.SYS_CONFIG, key = "#configKey")
  @Override
  public String selectConfigByKey(String configKey) {
    SysConfig retConfig = this.getOne(new QueryWrapper().eq(SysConfig::getConfigKey, configKey));
    return ObjectUtils.notNullGetter(retConfig, SysConfig::getConfigValue, StringUtils.EMPTY);
  }
}
