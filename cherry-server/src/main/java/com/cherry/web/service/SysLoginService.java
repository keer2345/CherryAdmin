package com.cherry.web.service;

import com.cherry.common.core.constant.TenantConstants;
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

}
