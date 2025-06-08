package com.cherry.common.core.service;

/**
 * 通用 用户服务
 *
 * @author Lion Li
 */
public interface UserService {

  /**
   * 通过用户ID查询用户账户
   *
   * @param userIds 用户ID 多个用逗号隔开
   * @return 用户名称
   */
  String selectNicknameByIds(String userIds);

  /**
   * 通过用户ID查询用户账户
   *
   * @param userId 用户ID
   * @return 用户名称
   */
  String selectNicknameById(Long userId);

  /**
   * 通过用户ID查询用户账户
   *
   * @param userId 用户ID
   * @return 用户账户
   */
  String selectUserNameById(Long userId);
}
