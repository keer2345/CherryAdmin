package com.cherry.system.service.impl;

import com.cherry.common.satoken.handler.LoginHelper;
import com.cherry.system.service.ISysMenuService;
import com.cherry.system.service.ISysPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * 用户权限处理
 *
 * @author keer
 * @date 2025-05-26
 */
@RequiredArgsConstructor
@Service
public class SysPermissionServiceImpl implements ISysPermissionService {

  private final ISysMenuService menuService;

  /**
   * 获取角色数据权限
   *
   * @param userId 用户id
   * @return 角色权限信息
   */
  @Override
  public Set<String> getRolePermission(Long userId) {
    return Set.of();
  }

  /**
   * 获取菜单数据权限
   *
   * @param userId 用户id
   * @return 菜单权限信息
   */
  @Override
  public Set<String> getMenuPermission(Long userId) {
    Set<String> perms = new HashSet<>();
    // 管理员拥有所有权限
    if (LoginHelper.isSuperAdmin(userId)) {
      perms.add("*:*:*");
    } else {
      perms.addAll(menuService.selectMenuPermsByUserId(userId));
    }

    return perms;
  }
}
