package com.cherry.web.service;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import com.cherry.common.core.constant.*;
import com.cherry.common.core.domain.dto.PostDTO;
import com.cherry.common.core.domain.dto.RoleDTO;
import com.cherry.common.core.domain.model.LoginUser;
import com.cherry.common.core.enums.LoginType;
import com.cherry.common.core.exception.user.UserException;
import com.cherry.common.core.utils.*;
import com.cherry.common.log.event.LogininforEvent;
import com.cherry.common.mybatis.helper.DataPermissionHelper;
import com.cherry.common.redis.utils.RedisUtils;
import com.cherry.common.satoken.utils.LoginHelper;
import com.cherry.common.tenant.exception.TenantException;
import com.cherry.common.tenant.helper.TenantHelper;
import com.cherry.system.domain.SysUser;
import com.cherry.system.domain.vo.*;
import com.cherry.system.mapper.SysUserMapper;
import com.cherry.system.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;

/**
 * 登录校验方法
 *
 * @author keer
 * @date 2025-05-23
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class SysLoginService {
  // todo

  @Value("${user.password.maxRetryCount}")
  private Integer maxRetryCount;

  @Value("${user.password.lockTime}")
  private Integer lockTime;

  private final ISysPermissionService permissionService;
  private final ISysRoleService roleService;
  private final ISysPostService postService;
  private final ISysDeptService deptService;
  private final ISysTenantService tenantService;
  private final SysUserMapper userMapper;

  /**
   * 校验租户
   *
   * @param tenantId 租户ID
   */
  public void checkTenant(String tenantId) {
    if (!TenantHelper.isEnable() || TenantConstants.DEFAULT_TENANT_ID.equals(tenantId)) {
      return;
    }
    if (StringUtils.isBlank(tenantId)) {
      throw new TenantException("tenant.number.not.blank");
    }

    SysTenantVo tenant = tenantService.queryByTenantId(tenantId);

    if (ObjUtil.isNull(tenant)) {
      log.info("登录租户：{} 不存在.", tenantId);
      throw new TenantException("tenant.not.exists");
    } else if (SystemConstants.DISABLE.equals(tenant.getStatus())) {
      log.info("登录租户：{} 已被停用.", tenantId);
      throw new TenantException("tenant.blocked");
    } else if (ObjectUtil.isNotNull(tenant.getExpireTime())
        && new Date().after(tenant.getExpireTime())) {
      log.info("登录租户：{} 已超过有效期.", tenantId);
      throw new TenantException("tenant.expired");
    }
  }

  /**
   * 记录登录信息
   *
   * @param tenantId 租户ID
   * @param username 用户名
   * @param status 状态
   * @param message 消息内容
   */
  public void recordLogininfor(String tenantId, String username, String status, String message) {
    LogininforEvent logininforEvent = new LogininforEvent();
    logininforEvent.setTenantId(tenantId);
    logininforEvent.setUsername(username);
    logininforEvent.setStatus(status);
    logininforEvent.setMessage(message);
    logininforEvent.setRequest(ServletUtils.getRequest());

    SpringUtils.context().publishEvent(logininforEvent);
  }

  /** 登录校验 */
  public void checkLogin(
      LoginType loginType, String tenantId, String username, Supplier<Boolean> supplier) {
    String errorKey = GlobalConstants.GLOBAL_REDIS_KEY + CacheConstants.PWD_ERR_CNT_KEY + username;
    String loginFail = Constants.LOGIN_FAIL;

    // 获取用户登录错误次数，默认为0 (可自定义限制策略 例如: key + username + ip)
    int errorNumber = ObjUtil.defaultIfNull(RedisUtils.getCacheObject(errorKey), 0);
    // 锁定时间内登录 则踢出
    if (errorNumber >= maxRetryCount) {
      recordLogininfor(
          tenantId,
          username,
          loginFail,
          MessageUtils.message(loginType.getRetryLimitExceed(), maxRetryCount, lockTime));
      throw new UserException(loginType.getRetryLimitExceed(), maxRetryCount, lockTime);
    }
    if (supplier.get()) {
      // 错误次数递增
      errorNumber++;
      RedisUtils.setCacheObject(errorKey, errorNumber, Duration.ofMinutes(lockTime));
      // 达到规定错误次数 则锁定登录
      if (errorNumber >= maxRetryCount) {
        recordLogininfor(
            tenantId,
            username,
            loginFail,
            MessageUtils.message(loginType.getRetryLimitExceed(), maxRetryCount, lockTime));
        throw new UserException(loginType.getRetryLimitExceed(), maxRetryCount, lockTime);
      } else {
        // 未达到规定错误次数
        recordLogininfor(
            tenantId,
            username,
            loginFail,
            MessageUtils.message(loginType.getRetryLimitCount(), errorNumber));
        throw new UserException(loginType.getRetryLimitCount(), errorNumber);
      }
    }
    // 登录成功 清空错误次数
    RedisUtils.deleteObject(errorKey);
  }

  /** 构建登录用户 */
  public LoginUser buildLoginUser(SysUserVo user) {
    LoginUser loginUser = new LoginUser();
    Long userId = user.getUserId();

    loginUser.setTenantId(user.getTenantId());
    loginUser.setUserId(userId);
    loginUser.setDeptId(user.getDeptId());
    loginUser.setUsername(user.getUserName());
    loginUser.setNickname(user.getNickName());
    loginUser.setUserType(user.getUserType());
    loginUser.setMenuPermission(permissionService.getMenuPermission(userId));
    loginUser.setRolePermission(permissionService.getRolePermission(userId));

    if (ObjUtil.isNotNull(user.getDeptId())) {
      Opt<SysDeptVo> deptOpt = Opt.of(user.getDeptId()).map(deptService::selectDeptById);
      loginUser.setDeptName(deptOpt.map(SysDeptVo::getDeptName).orElse(StringUtils.EMPTY));
      loginUser.setDeptCategory(deptOpt.map(SysDeptVo::getDeptCategory).orElse(StringUtils.EMPTY));
    }

    List<SysRoleVo> roles = roleService.selectRolesByUserId(userId);
    List<SysPostVo> posts = postService.selectPostsByUserId(userId);
    loginUser.setRoles(BeanUtil.copyToList(roles, RoleDTO.class));
    loginUser.setPosts(BeanUtil.copyToList(posts, PostDTO.class));

    recordLogininfor(
        loginUser.getTenantId(),
        loginUser.getUsername(),
        Constants.LOGIN_SUCCESS,
        MessageUtils.message("user.login.success"));

    return loginUser;
  }

  /** 退出登录 */
  public void logout() {
    try {
      LoginUser loginUser = LoginHelper.getLoginUser();
      if (ObjUtil.isNull(loginUser)) {
        return;
      }
      if (TenantHelper.isEnable() && LoginHelper.isSuperAdmin()) {
        // 超级管理员 登出清除动态租户
        TenantHelper.clearDynamic();
      }
      recordLogininfor(
          loginUser.getTenantId(),
          loginUser.getUsername(),
          Constants.LOGOUT,
          MessageUtils.message("user.logout.success"));
    } catch (NotLoginException ignored) {

    } finally {
      try {
        StpUtil.logout();
      } catch (NotLoginException ignored) {
      }
    }
  }

  /**
   * 记录登录信息
   *
   * @param userId 用户ID
   */
  public void recordLoginInfo(Long userId, String ip) {
    SysUser sysUser = new SysUser();
    sysUser.setUserId(userId);
    sysUser.setLoginIp(ip);
    sysUser.setLoginDate(DateUtils.getNowDate());
    sysUser.setUpdateBy(userId);
    DataPermissionHelper.ignore(() -> userMapper.updateById(sysUser));
  }
}
