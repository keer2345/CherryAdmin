package com.cherry.system.service;

import com.cherry.system.domain.SysPerm;
import com.mybatisflex.core.query.QueryWrapper;

import java.util.List;

/**
 * 通用 数据权限 服务
 *
 * @author keer
 */
public interface ISysDataScopeService {

  /**
   * 获取角色自定义权限
   *
   * @param userId 角色id
   * @return 部门id组
   */
  String getRoleCustom(String userId);

  /**
   * 获取部门及以下权限
   *
   * @param deptId 部门id
   * @return 部门id组
   */
  String getDeptAndChild(String deptId);

    /**
     * 构建SQL
     * @param qw
     */
  void getQueryWithDataScope(QueryWrapper qw);
}
