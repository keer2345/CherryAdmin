package com.cherry.system.service;

/**
 * 参数配置 服务层
 * @author keer
 * @date 2025-06-10
 */
public interface ISysConfigService {
    // todo
    /**
     * 根据键名查询参数配置信息
     *
     * @param configKey 参数键名
     * @return 参数键值
     */
    String selectConfigByKey(String configKey);
}
