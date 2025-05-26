package com.cherry.web.service;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.cherry.common.core.constant.CacheConstants;
import com.cherry.common.core.constant.Constants;
import com.cherry.common.core.constant.GlobalConstants;
import com.cherry.common.core.constant.TenantConstants;
import com.cherry.common.core.domain.model.LoginUser;
import com.cherry.common.core.enums.LoginType;
import com.cherry.common.core.exception.user.UserException;
import com.cherry.common.core.utils.MessageUtils;
import com.cherry.common.core.utils.ServletUtils;
import com.cherry.common.core.utils.SpringUtils;
import com.cherry.common.log.event.LogininforEvent;
import com.cherry.common.redis.utils.RedisUtils;
import com.cherry.common.tenant.helper.TenantHelper;
import com.cherry.system.domain.vo.SysUserVo;
import com.cherry.system.service.ISysPermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.constant.Constable;
import java.time.Duration;
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

  /**
   * 校验租户
   *
   * @param tenantId 租户ID
   */
  public void checkTenant(String tenantId) {
    if (!TenantHelper.isEnable() || TenantConstants.DEFAULT_TENANT_ID.equals(tenantId)) {
      return;
    }

    // todo
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
    // todo
      loginUser.setMenuPermission(permissionService.getMenuPermission(userId));

    return loginUser;
  }
}
