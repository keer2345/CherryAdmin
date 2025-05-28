package com.cherry.system.service;

import com.cherry.system.domain.vo.SysDeptVo;

/**
 * 部门管理 服务层
 *
 * @author keer
 * @date 2025-05-28
 */
public interface ISysDeptService {
  // todo

  /**
   * 根据部门ID查询信息
   *
   * @param deptId 部门ID
   * @return 部门信息
   */
  SysDeptVo selectDeptById(Long deptId);
}
