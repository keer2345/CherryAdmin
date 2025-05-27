package com.cherry.system.service;

import com.cherry.system.domain.vo.SysRoleVo;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 角色业务层
 *
 * @author keer
 * @date 2025-05-27
 */
public interface ISysRoleService {
  // todo

  /**
   * 根据用户ID查询角色权限
   *
   * @param userId 用户ID
   * @return 权限列表
   */
  Set<String> selectRolePermissionByUserId(Long userId);

  /**
   * 根据用户ID查询角色列表
   *
   * @param userId 用户ID
   * @return 角色列表
   */
  List<SysRoleVo> selectRolesByUserId(Long userId);
}
