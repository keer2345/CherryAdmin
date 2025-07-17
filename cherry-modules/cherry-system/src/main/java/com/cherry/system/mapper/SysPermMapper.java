package com.cherry.system.mapper;

import com.cherry.system.domain.SysPerm;
import com.cherry.system.domain.SysRole;
import com.cherry.system.domain.SysUserRole;
import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.query.QueryWrapper;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
 *
 * @author keer
 * @date 2025-05-27
 */
@Mapper
public interface SysPermMapper extends BaseMapper<SysPerm> {}
