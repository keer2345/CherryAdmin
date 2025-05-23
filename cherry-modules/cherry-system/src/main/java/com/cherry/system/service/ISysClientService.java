package com.cherry.system.service;

import com.cherry.system.domain.vo.SysClientVo;

/**
 * 客户端管理Service接口
 * @author keer
 * @date 2025-05-23
 */
public interface ISysClientService {
  // todo

    /**
     * 查询客户端信息基于客户端id
     */
    SysClientVo queryByClientId(String clientId);
}
