package com.cherry.system.mapper;

import com.cherry.system.domain.SysTenant;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 租户Mapper接口
 *
 * @author keer
 * @date 2025-05-22
 */
@Mapper
public interface SysTenantMapper extends BaseMapper<SysTenant> {}
