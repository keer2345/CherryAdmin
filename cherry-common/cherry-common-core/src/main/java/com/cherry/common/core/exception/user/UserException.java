package com.cherry.common.core.exception.user;

import com.cherry.common.core.exception.base.BaseException;

import java.io.Serial;

/**
 * @author keer
 * @date 2025-05-23
 */
public class UserException extends BaseException {

  @Serial private static final long serialVersionUID = 1L;

  public UserException(String code, Object... args) {
    super("user", code, args, null);
  }
}
