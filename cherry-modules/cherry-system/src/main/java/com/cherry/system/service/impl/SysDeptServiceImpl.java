package com.cherry.system.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cherry.common.core.service.DeptService;
import com.cherry.common.core.utils.ObjectUtils;
import com.cherry.common.core.utils.SpringUtils;
import com.cherry.common.core.utils.StringUtils;
import com.cherry.system.domain.SysDept;
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
}
