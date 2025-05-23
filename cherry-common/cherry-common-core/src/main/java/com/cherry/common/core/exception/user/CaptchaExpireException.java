package com.cherry.common.core.exception.user;

import java.io.Serial;

/**
 * @author keer
 * @date 2025-05-23
 */
public class CaptchaExpireException extends UserException {
  @Serial private static final long serialVersionUID = 1L;

  public CaptchaExpireException() {
    super("user.jcaptcha.error");
  }
}
