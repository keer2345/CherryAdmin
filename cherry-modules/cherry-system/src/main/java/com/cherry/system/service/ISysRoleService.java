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
   * 根据条件查询角色数据
   *
   * @param role 角色信息
   * @return 角色数据集合信息
   */
  List<SysRoleVo> selectRoleListByLoginUser(SysRoleBo role);

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

    /**
     * 校验角色是否允许操作
     *
     * @param role 角色信息
     */
    void checkRoleAllowed(SysRoleBo role);

    /**
     * 校验角色是否有数据权限
     *
     * @param roleId 角色id
     */
    void checkRoleDataScope(String roleId);

    /**
     * 修改角色状态
     *
     * @param roleId 角色ID
     * @param status 角色状态
     * @return 结果
     */
    boolean updateRoleStatus(String roleId, String status);

    /**
     * 通过角色ID查询角色使用数量
     *
     * @param roleId 角色ID
     * @return 结果
     */
    long countUserRoleByRoleId(String roleId) ;
}
