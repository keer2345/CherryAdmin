package com.cherry.system.service;

import cn.hutool.core.lang.tree.Tree;
import com.cherry.system.domain.bo.SysDeptBo;
import com.cherry.system.domain.vo.SysDeptVo;

import java.util.List;

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

    /**
     * 查询部门树结构信息
     *
     * @param dept 部门信息
     * @return 部门树信息集合
     */
    List<Tree<Long>> selectDeptTreeList(SysDeptBo dept);

    /**
     * 构建前端所需要下拉树结构
     *
     * @param depts 部门列表
     * @return 下拉树结构列表
     */
    List<Tree<Long>> buildDeptTreeSelect(List<SysDeptVo> depts);

}
