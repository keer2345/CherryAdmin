package com.cherry.system.domain.vo;

import lombok.Data;

/**
 * 用户个人信息
 *
 * @author keer
 * @date 2025-06-06
 */
@Data
public class ProfileVo {
  // todo

  /** 用户信息 */
  private SysUserVo user;

  /** 用户所属角色组 */
  private String roleGroup;

  /** 用户所属岗位组 */
  private String postGroup;
}
