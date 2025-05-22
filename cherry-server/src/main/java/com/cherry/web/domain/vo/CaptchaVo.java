package com.cherry.web.domain.vo;

import lombok.Data;

/**
 * 验证码信息
 *
 * @author keer
 * @date 2025-05-22
 */
@Data
public class CaptchaVo {

  /** 是否开启验证码 */
  private Boolean captchaEnabled = true;

  private String uuid;

  /** 验证码图片 */
  private String img;
}
