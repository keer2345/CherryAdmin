package com.cherry.system.mapper;

import com.cherry.system.domain.SysRole;
import com.cherry.system.domain.SysUserRole;
import com.cherry.system.domain.vo.SysRoleVo;
import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.query.QueryWrapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 角色表 数据层
 *
 * @author keer
 * @date 2025-05-27
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {
  // todo

  /**
   * 根据用户ID查询角色
   *
   * @param userId 用户ID
   * @return 角色列表
   */
  default List<SysRole> selectRolesByUserId(String userId) {
    QueryWrapper queryWrapper = new QueryWrapper();
    queryWrapper
        //        .create()
        //        .select()
        .from(SysRole.class)
        //            .where(SysRole::getId).eq("3");
        .as("a")
        .leftJoin(SysUserRole.class)
        .as("b")
        .on(SysRole::getId, SysUserRole::getRoleId)
        //            .eq(SysUserRole::getUserId,userId);
        .where(SysUserRole::getUserId)
        .eq(userId);
    return this.selectListByQuery(queryWrapper);
  }
}
