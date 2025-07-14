package com.cherry.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.util.ObjectUtil;
import com.cherry.common.core.constant.SystemConstants;
import com.cherry.common.core.service.DeptService;
import com.cherry.common.core.utils.*;
import com.cherry.common.flex.helper.DataBaseHelper;
import com.cherry.system.domain.SysDept;
import com.cherry.system.domain.bo.SysDeptBo;
import com.cherry.system.domain.vo.SysDeptVo;
import com.cherry.system.mapper.SysDeptMapper;
import com.cherry.system.service.ISysDeptService;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 部门管理 服务实现
 *
 * @author keer
 * @date 2025-05-28
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept>
    implements ISysDeptService, DeptService {
  // todo

  private final SysDeptMapper deptMapper;

  // todo
  // @Cacheable(cacheNames = CacheNames.SYS_DEPT, key = "#deptId")
  @Override
  public SysDeptVo selectDeptById(String deptId) {
    Optional<SysDept> sysDeptOpt = this.getByIdOpt(deptId);
    deptMapper.selectOneById(deptId);
    if (sysDeptOpt.isEmpty()) {
      return null;
    }
    SysDeptVo dept = MapstructUtils.convert(sysDeptOpt.get(), SysDeptVo.class);

    SysDeptVo parentDept =
        this.getOneAs(new QueryWrapper().eq(SysDept::getId, dept.getParentId()), SysDeptVo.class);
    dept.setParentName(parentDept.getDeptName());
    return dept;
  }

  /**
   * 通过部门ID查询部门名称
   *
   * @param deptIds 部门ID串逗号分隔
   * @return 部门名称串逗号分隔
   */
  @Override
  public String selectDeptNameByIds(String deptIds) {
    List<String> list = new ArrayList<>();
    //        for (Long id : StringUtils.splitTo(deptIds, Convert::toLong)) {
    for (String id : StringUtils.splitTo(deptIds, Convert::toStr)) {
      SysDeptVo vo = SpringUtils.getAopProxy(this).selectDeptById(id);
      if (ObjectUtil.isNotNull(vo)) {
        list.add(vo.getDeptName());
      }
    }
    return String.join(StringUtils.SEPARATOR, list);
  }

  /**
   * 查询部门树结构信息
   *
   * @param bo 部门信息
   * @return 部门树信息集合
   */
  @Override
  public List<Tree<String>> selectDeptTreeList(SysDeptBo bo) {
    QueryWrapper qw = buildQueryWrapper(bo);
    List<SysDeptVo> depts = this.listAs(qw, SysDeptVo.class);
    //        MapstructUtils.convert(deptMapper.selectDeptList(qw), SysDeptVo.class);
    return buildDeptTreeSelect(depts);
  }

  private QueryWrapper buildQueryWrapper(SysDeptBo bo) {
    QueryWrapper qw = new QueryWrapper().create().from(SysDept.class);
    qw.eq(SysDept::getId, bo.getId());
    qw.eq(SysDept::getParentId, bo.getParentId());
    qw.like(SysDept::getDeptName, bo.getDeptName());
    qw.like(SysDept::getDeptCategory, bo.getDeptCategory());
    qw.eq(SysDept::getStatus, bo.getStatus());

    if (ObjectUtil.isNotNull(bo.getBelongDeptId())) {
      // 部门树搜索
      qw.and(
          x -> {
            String parentId = bo.getBelongDeptId();
            List<SysDept> deptList = this.selectListByParentId(parentId);
            List<String> deptIds = StreamUtils.toList(deptList, SysDept::getId);
            deptIds.add(parentId);
            x.in(SysDept::getId, deptIds);
          });
    }

    qw.orderBy(SysDept::getAncestors);
    qw.orderBy(SysDept::getTop);
    qw.orderBy(SysDept::getOrderNum);
    qw.orderBy(SysDept::getCreateTime);
    return qw;
  }

  /**
   * 构建前端所需要下拉树结构
   *
   * @param depts 部门列表
   * @return 下拉树结构列表
   */
  @Override
  public List<Tree<String>> buildDeptTreeSelect(List<SysDeptVo> depts) {
    if (CollUtil.isEmpty(depts)) {
      return CollUtil.newArrayList();
    }
    return TreeBuildUtils.buildMultiRoot(
        depts,
        SysDeptVo::getId,
        SysDeptVo::getParentId,
        (node, treeNode) ->
            treeNode
                .setId(node.getId())
                .setParentId(node.getParentId())
                .setName(node.getDeptName())
                .setWeight(node.getOrderNum())
                .putExtra("disabled", SystemConstants.DISABLE.equals((node.getStatus()))));
  }

  /**
   * 根据父部门ID查询其所有子部门的列表
   *
   * @param parentId 父部门ID
   * @return 包含子部门的列表
   */
  @Override
  public List<SysDept> selectListByParentId(String parentId) {
    return this.list(
        new QueryWrapper()
            .select(SysDept::getId)
            .where(DataBaseHelper.findInSet(parentId, "ancestors")));
  }
}
