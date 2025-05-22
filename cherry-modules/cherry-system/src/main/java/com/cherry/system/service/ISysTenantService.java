package com.cherry.system.service;

import com.cherry.system.domain.bo.SysTenantBo;
import com.cherry.system.domain.vo.SysTenantVo;

import java.util.List;

/**
 * 租户Service接口
 * @author keer
 * @date 2025-05-22
 */
public interface ISysTenantService {
    // todo

    /**
     * 查询租户列表
     */
    List<SysTenantVo> queryList(SysTenantBo bo);
}
