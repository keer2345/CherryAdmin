package com.cherry.system.service;

import java.util.Collection;
import java.util.Set;

/**
 * 菜单 业务层
 *
 * @author keer
 * @date 2025-05-26
 */
public interface ISysMenuService {
  // todo

  /**
   * 根据用户ID查询权限
   *
   * @param userId 用户ID
   * @return 权限列表
   */
  Set<String> selectMenuPermsByUserId(Long userId);
}
