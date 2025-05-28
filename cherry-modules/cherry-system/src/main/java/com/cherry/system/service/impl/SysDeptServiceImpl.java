package com.cherry.system.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cherry.common.core.utils.ObjectUtils;
import com.cherry.system.domain.SysDept;
import com.cherry.system.domain.vo.SysDeptVo;
import com.cherry.system.mapper.SysDeptMapper;
import com.cherry.system.service.ISysDeptService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 部门管理 服务实现
 *
 * @author keer
 * @date 2025-05-28
 */
@RequiredArgsConstructor
@Service
public class SysDeptServiceImpl implements ISysDeptService {
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
}
