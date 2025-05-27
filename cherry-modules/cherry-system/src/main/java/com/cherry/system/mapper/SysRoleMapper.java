package com.cherry.system.mapper;

import com.cherry.common.mybatis.core.mapper.BaseMapperPlus;
import com.cherry.system.domain.SysRole;
import com.cherry.system.domain.vo.SysRoleVo;

import java.util.List;

/**
 * 角色表 数据层
 *
 * @author keer
 * @date 2025-05-27
 */
public interface SysRoleMapper extends BaseMapperPlus<SysRole, SysRoleVo> {
  // todo

  /**
   * 根据用户ID查询角色
   *
   * @param userId 用户ID
   * @return 角色列表
   */
  List<SysRoleVo> selectRolesByUserId(Long userId);
}
