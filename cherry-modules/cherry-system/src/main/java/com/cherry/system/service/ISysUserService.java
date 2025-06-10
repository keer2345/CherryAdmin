package com.cherry.system.service;

import com.cherry.system.domain.bo.SysUserBo;
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

  /**
   * 根据用户ID查询用户所属角色组
   *
   * @param userId 用户ID
   * @return 结果
   */
  String selectUserRoleGroup(Long userId);

  /**
   * 根据用户ID查询用户所属岗位组
   *
   * @param userId 用户ID
   * @return 结果
   */
  String selectUserPostGroup(Long userId);

    /**
     * 校验手机号码是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    boolean checkPhoneUnique(SysUserBo user);

    /**
     * 校验email是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    boolean checkEmailUnique(SysUserBo user);

    /**
     * 修改用户基本信息
     *
     * @param user 用户信息
     * @return 结果
     */
    int updateUserProfile(SysUserBo user);


    /**
     * 重置用户密码
     *
     * @param userId   用户ID
     * @param password 密码
     * @return 结果
     */
    int resetUserPwd(Long userId, String password);
}
