package com.cherry.web.service;

import com.cherry.common.core.constant.TenantConstants;
import com.cherry.common.core.utils.ServletUtils;
import com.cherry.common.core.utils.SpringUtils;
import com.cherry.common.log.event.LogininforEvent;
import com.cherry.common.tenant.helper.TenantHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
}
