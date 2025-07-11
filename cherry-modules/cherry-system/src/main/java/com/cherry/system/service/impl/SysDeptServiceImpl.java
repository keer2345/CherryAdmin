package com.cherry.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.util.ObjectUtil;
import com.cherry.common.core.constant.SystemConstants;
import com.cherry.common.core.service.DeptService;
import com.cherry.common.core.utils.*;
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
import org.springframework.stereotype.Service;

/**
 * 部门管理 服务实现
 *
 * @author keer
 * @date 2025-05-28
 */
@RequiredArgsConstructor
@Service
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
      //    if (ObjUtil.isNull(sysDept)) {
      return null;
    }
    SysDeptVo dept = MapstructUtils.convert(sysDeptOpt.get(), SysDeptVo.class);

    SysDeptVo parentDept =
        this.getOneAs(new QueryWrapper().eq(SysDept::getId, dept.getParentId()), SysDeptVo.class);
    dept.setParentName(parentDept.getDeptName());
    //        deptMapper.selectVoOne(
    //            new LambdaQueryWrapper<SysDept>()
    //                .select(SysDept::getDeptName)
    //                .eq(SysDept::getDeptId, dept.getParentId()));
    //        dept.setParentName(ObjectUtils.notNullGetter(parentDept, SysDeptVo::getDeptName));
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
    qw.orderBy(SysDept::getAncestors);
    //        qw.orderBy(SysDept::getParentId);
      qw.orderBy(SysDept::getTop);
    qw.orderBy(SysDept::getOrderNum);
    qw.orderBy(SysDept::getCreateTime);
    //        qw.orderBy(SysDept::getId);
    if (ObjectUtil.isNotNull(bo.getBelongDeptId())) {
      // 部门树搜索
      qw.and(
          x -> {
            String parentId = bo.getBelongDeptId();
            List<SysDept> deptList =
                this.list(new QueryWrapper().eq(SysDept::getParentId, parentId));
            List<String> deptIds = StreamUtils.toList(deptList, SysDept::getId);
            deptIds.add(parentId);
            x.in(SysDept::getId, deptIds);
          });
    }
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
    // 获取当前列表中每一个节点的parentId，然后在列表中查找是否有id与其parentId对应，若无对应，则表明此时节点列表中，该节点在当前列表中属于顶级节点
    List<Tree<String>> treeList = CollUtil.newArrayList();

    for (SysDeptVo d : depts) {
      String parentId = d.getParentId();
      SysDeptVo sysDeptVo = StreamUtils.findFirst(depts, it -> it.getId() == parentId);
      if (ObjectUtil.isNull(sysDeptVo)) {
        List<Tree<String>> trees =
            TreeBuildUtils.build(
                depts,
                parentId,
                (dept, tree) ->
                    tree.setId(dept.getId())
                        .setParentId(dept.getParentId())
                        .setName(dept.getDeptName())
                        .setWeight(dept.getOrderNum())
                        .putExtra("disabled", SystemConstants.DISABLE.equals(dept.getStatus())));
        Tree<String> tree = StreamUtils.findFirst(trees, it -> it.getId() == d.getId());
        treeList.add(tree);
      }
    }
    return treeList;
  }
}
