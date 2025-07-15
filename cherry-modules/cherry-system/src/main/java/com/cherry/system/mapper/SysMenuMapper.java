package com.cherry.system.mapper;

import com.cherry.common.core.constant.SystemConstants;
import com.cherry.system.domain.SysMenu;
import com.cherry.system.domain.SysRole;
import com.cherry.system.domain.SysRoleMenu;
import com.cherry.system.domain.SysUserRole;
import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.query.QueryWrapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 菜单表 数据层
 *
 * @author keer
 * @date 2025-05-26
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {
  // todo

  /**
   * 根据用户ID查询权限
   *
   * @param userId 用户ID
   * @return 权限列表
   */
  default List<SysMenu> selectMenuPermsByUserId(String userId) {

    QueryWrapper query =
        QueryWrapper.create()
            .select()
            .from(SysMenu.class)
            .as("a")
            .leftJoin(SysRoleMenu.class)
            .as("b")
            .on(SysMenu::getId, SysRoleMenu::getMenuId)
            .leftJoin(SysUserRole.class)
            .as("c")
            .on(SysUserRole::getRoleId, SysRoleMenu::getRoleId)
            .leftJoin(SysRole.class)
            .as("d")
            .on(SysRole::getId, SysUserRole::getRoleId)
            .where(SysMenu::getPerms)
            .ne("")
            .eq(SysUserRole::getUserId, userId)
            .eq(SysMenu::getStatus, SystemConstants.NORMAL)
            .eq(SysRole::getStatus, SystemConstants.NORMAL);

    return selectListByQuery(query);
  }

  /**
   * 查询所有菜单
   *
   * @return 菜单列表
   */
  default List<SysMenu> selectMenuTreeAll() {
    QueryWrapper query =
        QueryWrapper.create()
            .select()
            .from(SysMenu.class)
            .where(SysMenu::getMenuType)
            .in(SystemConstants.TYPE_DIR, SystemConstants.TYPE_MENU)
            .eq(SysMenu::getStatus, SystemConstants.NORMAL)
            .orderBy(SysMenu::getTop, true)
            .orderBy(SysMenu::getOrderNum, true);
    return this.selectListByQuery(query);
  }

  /**
   * 根据用户ID查询菜单
   *
   * @param userId 用户ID
   * @return 菜单列表
   */
  default List<SysMenu> selectMenuTreeByUserId(String userId) {
    QueryWrapper query =
        QueryWrapper.create()
            .select()
            .from(SysMenu.class)
            .as("a")
            .leftJoin(SysRoleMenu.class)
            .as("b")
            .on(SysMenu::getId, SysRoleMenu::getMenuId)
            .leftJoin(SysUserRole.class)
            .as("c")
            .on(SysUserRole::getRoleId, SysRoleMenu::getRoleId)
            .leftJoin(SysRole.class)
            .as("d")
            .on(SysRole::getId, SysUserRole::getRoleId)
            .where(SysMenu::getMenuType)
            .in(SystemConstants.TYPE_DIR, SystemConstants.TYPE_MENU)
            .eq(SysMenu::getStatus, SystemConstants.NORMAL)
            .eq(SysRole::getStatus, SystemConstants.NORMAL)
            .eq(SysUserRole::getUserId, userId)
            .orderBy(SysMenu::getTop, true)
            .orderBy(SysMenu::getOrderNum, true);

    return this.selectListByQuery(query);
  }
}
