package com.cherry.system.service;

import com.cherry.common.flex.core.page.PageQuery;
import com.cherry.common.flex.core.page.TableDataInfo;
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
  SysUserVo selectUserById(String userId);

  /**
   * 根据用户ID查询用户所属角色组
   *
   * @param userId 用户ID
   * @return 结果
   */
  String selectUserRoleGroup(String userId);

  /**
   * 根据用户ID查询用户所属岗位组
   *
   * @param userId 用户ID
   * @return 结果
   */
  String selectUserPostGroup(String userId);

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
   * @param userId 用户ID
   * @param password 密码
   * @return 结果
   */
  int resetUserPwd(String userId, String password);

  /**
   * 根据条件分页查询用户列表
   *
   * @param user 用户信息
   * @param pageQuery 发呢也
   * @return 用户信息
   */
  TableDataInfo<SysUserVo> selectPageUserList(SysUserBo user, PageQuery pageQuery);

  /**
   * 校验用户是否允许操作
   *
   * @param userId 用户ID
   */
  void checkUserAllowed(String userId);

  /**
   * 校验用户是否有数据权限
   *
   * @param userId 用户id
   */
  void checkUserDataScope(String userId);

  /**
   * 校验用户名称是否唯一
   *
   * @param user 用户信息
   * @return 结果
   */
  boolean checkUserNameUnique(SysUserBo user);

  /**
   * 修改用户信息
   *
   * @param user 用户信息
   * @return 结果
   */
  int updateUser(SysUserBo user);

  /**
   * 修改用户状态
   *
   * @param userId 用户ID
   * @param status 帐号状态
   * @return 结果
   */
  boolean updateUserStatus(String userId, String status);

  /**
   * 新增用户信息
   *
   * @param user 用户信息
   * @return 结果
   */
  int insertUser(SysUserBo user);
}
