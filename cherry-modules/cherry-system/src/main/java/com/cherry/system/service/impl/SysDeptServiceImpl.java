package com.cherry.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cherry.common.core.constant.SystemConstants;
import com.cherry.common.core.service.DeptService;
import com.cherry.common.core.utils.*;
import com.cherry.system.domain.SysDept;
import com.cherry.system.domain.bo.SysDeptBo;
import com.cherry.system.domain.vo.SysDeptVo;
import com.cherry.system.mapper.SysDeptMapper;
import com.cherry.system.service.ISysDeptService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 部门管理 服务实现
 *
 * @author keer
 * @date 2025-05-28
 */
@RequiredArgsConstructor
@Service
public class SysDeptServiceImpl implements ISysDeptService, DeptService {
  // todo

  private final SysDeptMapper baseMapper;

  // todo
  // @Cacheable(cacheNames = CacheNames.SYS_DEPT, key = "#deptId")
  @Override
  public SysDeptVo selectDeptById(Long deptId) {
    SysDeptVo dept = baseMapper.selectVoById(deptId);
    if (ObjUtil.isNull(dept)) {
      return null;
    }
    SysDeptVo parentDept =
        baseMapper.selectVoOne(
            new LambdaQueryWrapper<SysDept>()
                .select(SysDept::getDeptName)
                .eq(SysDept::getDeptId, dept.getParentId()));
    dept.setParentName(ObjectUtils.notNullGetter(parentDept, SysDeptVo::getDeptName));
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
    for (Long id : StringUtils.splitTo(deptIds, Convert::toLong)) {
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
  public List<Tree<Long>> selectDeptTreeList(SysDeptBo bo) {
    LambdaQueryWrapper<SysDept> lqw = buildQueryWrapper(bo);
    List<SysDeptVo> depts = baseMapper.selectDeptList(lqw);
    return buildDeptTreeSelect(depts);
  }

  private LambdaQueryWrapper<SysDept> buildQueryWrapper(SysDeptBo bo) {
    LambdaQueryWrapper<SysDept> lqw = Wrappers.lambdaQuery();
    lqw.eq(SysDept::getDelFlag, SystemConstants.NORMAL);
    lqw.eq(ObjectUtil.isNotNull(bo.getDeptId()), SysDept::getDeptId, bo.getDeptId());
    lqw.eq(ObjectUtil.isNotNull(bo.getParentId()), SysDept::getParentId, bo.getParentId());
    lqw.like(StringUtils.isNotBlank(bo.getDeptName()), SysDept::getDeptName, bo.getDeptName());
    lqw.like(
        StringUtils.isNotBlank(bo.getDeptCategory()),
        SysDept::getDeptCategory,
        bo.getDeptCategory());
    lqw.eq(StringUtils.isNotBlank(bo.getStatus()), SysDept::getStatus, bo.getStatus());
    lqw.orderByAsc(SysDept::getAncestors);
    lqw.orderByAsc(SysDept::getParentId);
    lqw.orderByAsc(SysDept::getOrderNum);
    lqw.orderByAsc(SysDept::getDeptId);
    if (ObjectUtil.isNotNull(bo.getBelongDeptId())) {
      // 部门树搜索
      lqw.and(
          x -> {
            Long parentId = bo.getBelongDeptId();
            List<SysDept> deptList = baseMapper.selectListByParentId(parentId);
            List<Long> deptIds = StreamUtils.toList(deptList, SysDept::getDeptId);
            deptIds.add(parentId);
            x.in(SysDept::getDeptId, deptIds);
          });
    }
    return lqw;
  }

    /**
     * 构建前端所需要下拉树结构
     *
     * @param depts 部门列表
     * @return 下拉树结构列表
     */
    @Override
    public List<Tree<Long>> buildDeptTreeSelect(List<SysDeptVo> depts) {
        if (CollUtil.isEmpty(depts)) {
            return CollUtil.newArrayList();
        }
        // 获取当前列表中每一个节点的parentId，然后在列表中查找是否有id与其parentId对应，若无对应，则表明此时节点列表中，该节点在当前列表中属于顶级节点
        List<Tree<Long>> treeList = CollUtil.newArrayList();
        for (SysDeptVo d : depts) {
            Long parentId = d.getParentId();
            SysDeptVo sysDeptVo = StreamUtils.findFirst(depts, it -> it.getDeptId().longValue() == parentId);
            if (ObjectUtil.isNull(sysDeptVo)) {
                List<Tree<Long>> trees = TreeBuildUtils.build(depts, parentId, (dept, tree) ->
                    tree.setId(dept.getDeptId())
                        .setParentId(dept.getParentId())
                        .setName(dept.getDeptName())
                        .setWeight(dept.getOrderNum())
                        .putExtra("disabled", SystemConstants.DISABLE.equals(dept.getStatus())));
                Tree<Long> tree = StreamUtils.findFirst(trees, it -> it.getId().longValue() == d.getDeptId());
                treeList.add(tree);
            }
        }
        return treeList;
    }
}
