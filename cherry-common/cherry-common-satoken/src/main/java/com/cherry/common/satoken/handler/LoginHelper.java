package com.cherry.common.satoken.handler;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.cherry.common.core.constant.SystemConstants;
import com.cherry.common.core.domain.model.LoginUser;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 登录鉴权助手
 *
 * <p>user_type 为 用户类型 同一个用户表 可以有多种用户类型 例如 pc,app deivce 为 设备类型 同一个用户类型 可以有 多种设备类型 例如 web,ios 可以组成
 * 用户类型与设备类型多对多的 权限灵活控制
 *
 * <p>多用户体系 针对 多种用户类型 但权限控制不一致 可以组成 多用户类型表与多设备类型 分别控制权限
 *
 * @author keer
 * @date 2025-05-23
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginHelper {
  // todo

  public static final String LOGIN_USER_KEY = "loginUser";
  public static final String TENANT_KEY = "tenantId";
  public static final String USER_KEY = "userId";
  public static final String USER_NAME_KEY = "userName";
  public static final String DEPT_KEY = "deptId";
  public static final String DEPT_NAME_KEY = "deptName";
  public static final String DEPT_CATEGORY_KEY = "deptCategory";
  public static final String CLIENT_KEY = "clientid";

  /** 获取用户(多级缓存) */
  @SuppressWarnings("unchecked cast")
  public static <T extends LoginUser> T getLoginUser() {
    SaSession session = StpUtil.getTokenSession();
    if (ObjectUtil.isNull(session)) {
      return null;
    }
    return (T) session.get(LOGIN_USER_KEY);
  }

  /**
   * 是否为超级管理员
   *
   * @return 结果
   */
  public static boolean isSuperAdmin() {
    return isSuperAdmin(getUserId());
  }

  /**
   * 是否为超级管理员
   *
   * @param userId 用户ID
   * @return 结果
   */
  public static boolean isSuperAdmin(Long userId) {
    return SystemConstants.SUPER_ADMIN_ID.equals(userId);
  }

  /** 获取用户id */
  public static Long getUserId() {
    return Convert.toLong(getExtra(USER_KEY));
  }

  /** 获取用户id */
  public static String getUserIdStr() {
    return Convert.toStr(getExtra(USER_KEY));
  }

  /** 获取用户账户 */
  public static String getUsername() {
    return Convert.toStr(getExtra(USER_NAME_KEY));
  }

  /** 获取租户ID */
  public static String getTenantId() {
    return Convert.toStr(getExtra(TENANT_KEY));
  }

  /** 获取部门ID */
  public static Long getDeptId() {
    return Convert.toLong(getExtra(DEPT_KEY));
  }

  /** 获取部门名 */
  public static String getDeptName() {
    return Convert.toStr(getExtra(DEPT_NAME_KEY));
  }

  /** 获取部门类别编码 */
  public static String getDeptCategory() {
    return Convert.toStr(getExtra(DEPT_CATEGORY_KEY));
  }

  /**
   * 获取当前 Token 的扩展信息
   *
   * @param key 键值
   * @return 对应的扩展数据
   */
  private static Object getExtra(String key) {
    try {
      return StpUtil.getExtra(key);
    } catch (Exception e) {
      return null;
    }
  }
}
