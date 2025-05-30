package com.cherry.common.tenant.exception;

import com.cherry.common.core.exception.base.BaseException;

import java.io.Serial;

/**
 * 租户异常类
 *
 * @author keer
 * @date 2025-05-30
 */
public class TenantException extends BaseException {
  @Serial private static final long serialVersionUID = 1L;

  public TenantException(String code, Object... args) {
    super("tenant", code, args, null);
  }
}
