package com.cherry.system.service;

import com.cherry.system.domain.vo.SysUserVo;

/**
 * 用户 业务层
 *
 * @author keer
 * @date 2025-05-28
 */
public interface ISysUserService {
  // todo

  /**
   * 通过用户ID查询用户
   *
   * @param userId 用户ID
   * @return 用户对象信息
   */
  SysUserVo selectUserById(Long userId);
}
