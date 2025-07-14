package com.cherry.system.service;

import com.cherry.common.flex.core.page.PageQuery;
import com.cherry.common.flex.core.page.TableDataInfo;
import com.cherry.system.domain.bo.SysRoleBo;
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
   * 根据条件分页查询角色数据
   *
   * @param role 角色信息
   * @return 角色数据集合信息
   */
  TableDataInfo<SysRoleVo> selectPageRoleList(SysRoleBo role, PageQuery pageQuery);

  /**
   * 根据条件查询角色数据
   *
   * @param role 角色信息
   * @return 角色数据集合信息
   */
  List<SysRoleVo> selectRoleList(SysRoleBo role);

  /**
   * 根据用户ID查询角色权限
   *
   * @param userId 用户ID
   * @return 权限列表
   */
  Set<String> selectRolePermissionByUserId(String userId);

  /**
   * 根据用户ID查询角色列表
   *
   * @param userId 用户ID
   * @return 角色列表
   */
  List<SysRoleVo> selectRolesByUserId(String userId);
}
