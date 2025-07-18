package com.cherry.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.cherry.common.core.constant.SystemConstants;
import com.cherry.common.core.domain.dto.RoleDTO;
import com.cherry.common.core.domain.model.LoginUser;
import com.cherry.common.core.exception.ServiceException;
import com.cherry.common.core.utils.StreamUtils;
import com.cherry.common.flex.helper.DataBaseHelper;
import com.cherry.common.satoken.utils.LoginHelper;
import com.cherry.system.domain.*;
import com.cherry.system.mapper.SysDeptMapper;
import com.cherry.system.mapper.SysPermMapper;
import com.cherry.system.mapper.SysRoleDeptMapper;
import com.cherry.system.mapper.SysRolePermMapper;
import com.cherry.system.service.ISysDataScopeService;
import com.mybatisflex.core.query.CPI;
import com.mybatisflex.core.query.QueryTable;
import com.mybatisflex.core.query.QueryWrapper;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
// @Service("sdss")
@Service
@Slf4j
public class SysDataScopeServiceImpl implements ISysDataScopeService {

  private final SysRoleDeptMapper roleDeptMapper;
  private final SysDeptMapper deptMapper;
  private final SysPermMapper permMapper;

  @Override
  public String getRoleCustom(String roleId) {
    QueryWrapper qw =
        QueryWrapper.create()
            .select(SysRoleDept::getDeptId)
            .from(SysRoleDept.class)
            .where(SysRoleDept::getRoleId)
            .eq(roleId);
    List<SysRoleDept> list = roleDeptMapper.selectListByQuery(qw);
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

  @Override
  public void getQueryWithDataScope(QueryWrapper qw) {
    log.info("getQueryWithDataScope begin");
    LoginUser user = LoginHelper.getLoginUser();
    // 如果是超级管理员或租户管理员，则不过滤数据
    if (LoginHelper.isSuperAdmin() || LoginHelper.isTenantAdmin()) {
      return;
    }
    if (ObjUtil.isNotNull(user)) {
      List<RoleDTO> roleDTOList = user.getRoles();
      //            获取角色ID
      List<String> roleIds = roleDTOList.stream().map(RoleDTO::getId).collect(Collectors.toList());

      List<SysPerm> permList;

      if (ObjUtil.isNotNull(roleDTOList)) {
        QueryWrapper qw1 =
            QueryWrapper.create()
                .select()
                .from(SysPerm.class)
                .as("a")
                .leftJoin(SysRolePerm.class)
                .as("b")
                .on(SysPerm::getId, SysRolePerm::getPermissionId)
                .leftJoin(SysRole.class)
                .on(SysRole::getId, SysRolePerm::getRoleId)
                .where(SysRole::getId)
                .in(roleIds)
                .eq(SysRole::getStatus, SystemConstants.NORMAL)
                .eq(SysPerm::getStatus, SystemConstants.NORMAL);

        permList = permMapper.selectListByQuery(qw1);

        if (CollUtil.isEmpty(permList)) {
          log.info("角色异常");
          throw new ServiceException("角色数据范围异常，没有对应的数据权限");
        }

        //          获取数据权限KEY
        List<String> permKeys = StreamUtils.toList(permList, SysPerm::getPermissionKey);
        if (CollUtil.contains(permKeys, "ALL")) {
          return;
        }
        if (CollUtil.contains(permKeys, "DEPT_AND_CHILD")) {
          StringBuilder sql = new StringBuilder();

            String deptColumnName = "dept_id";
            String tableName="";
          // 获取已构建的 queryWrapper 的表名
          List<QueryTable> queryTables = CPI.getQueryTables(qw);
          if (ObjUtil.isNotNull(queryTables)) {
            tableName = CPI.getQueryTables(qw).get(0).getName();
          }
          if (StrUtil.equals(tableName.toLowerCase(), "sys_dept")) {
            deptColumnName = "id";
          }
          sql.append(" ( ");

          sql.append(deptColumnName);
          sql.append(" in ( " + this.getDeptAndChild(user.getDeptId()) + " ) ");

          sql.append(" ) ");

          qw.and(sql.toString());
          log.info("qw2: {}", qw.toSQL());
          return;
        }
      }
    }
  }
}
