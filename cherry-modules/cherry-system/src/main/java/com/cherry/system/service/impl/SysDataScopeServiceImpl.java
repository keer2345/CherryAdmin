package com.cherry.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.cherry.common.core.utils.StreamUtils;
import com.cherry.common.flex.helper.DataBaseHelper;
import com.cherry.system.domain.SysDept;
import com.cherry.system.domain.SysRoleDept;
import com.cherry.system.mapper.SysDeptMapper;
import com.cherry.system.mapper.SysRoleDeptMapper;
import com.cherry.system.service.ISysDataScopeService;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 数据权限 实现
 *
 * <p>注意: 此Service内不允许调用标注`数据权限`注解的方法
 *
 * <p>例如: deptMapper.selectList 此 selectList 方法标注了`数据权限`注解 会出现循环解析的问题
 *
 * @author keer
 */
@RequiredArgsConstructor
@Service("sdss")
@Slf4j
public class SysDataScopeServiceImpl implements ISysDataScopeService {

  private final SysRoleDeptMapper roleDeptMapper;
  private final SysDeptMapper deptMapper;

  @Override
  public String getRoleCustom(String roleId) {
      QueryWrapper qw =
          QueryWrapper.create()
              .select(SysRoleDept::getDeptId)
              .from(SysRoleDept.class)
              .where(SysRoleDept::getRoleId)
              .eq(roleId);
    List<SysRoleDept> list =
        roleDeptMapper.selectListByQuery(
            qw
        );
    if (CollUtil.isNotEmpty(list)) {
      return StreamUtils.join(list, rd -> Convert.toStr(rd.getDeptId()));
    }
    return null;
  }

  @Override
  public String getDeptAndChild(String deptId) {
    List<SysDept> deptList =
        deptMapper.selectListByQuery(
            QueryWrapper.create()
                .select(SysDept::getId)
                .from(SysDept.class)
                .where(DataBaseHelper.findInSet(deptId, "ancestors")));
    List<String> ids = StreamUtils.toList(deptList, SysDept::getId);
    ids.add(deptId);
    if (CollUtil.isNotEmpty(ids)) {
      ids.replaceAll(s -> "'" + s + "'");
      return StreamUtils.join(ids, Convert::toStr);
    }
    return null;
  }
}
