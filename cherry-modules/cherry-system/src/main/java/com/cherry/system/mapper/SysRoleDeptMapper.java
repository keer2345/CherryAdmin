package com.cherry.system.mapper;

import com.cherry.system.domain.SysRole;
import com.cherry.system.domain.SysRoleDept;
import com.cherry.system.domain.SysUserRole;
import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.query.QueryWrapper;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色表 数据层
 *
 * @author keer
 * @date 2025-05-27
 */
@Mapper
public interface SysRoleDeptMapper extends BaseMapper<SysRoleDept> {}
