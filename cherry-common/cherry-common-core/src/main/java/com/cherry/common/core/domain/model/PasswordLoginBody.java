package com.cherry.common.core.domain.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 密码登录对象
 *
 * @author keer
 * @date 2025-05-23
 */
@Data
public class PasswordLoginBody extends LoginBody {

  /** 用户名 */
  @NotBlank(message = "{user.username.not.blank}")
  @Length(min = 2, max = 30, message = "{user.username.length.valid}")
  private String username;

  /** 用户密码 */
  @NotBlank(message = "{user.password.not.blank}")
  @Length(min = 5, max = 30, message = "{user.password.length.valid}")
  private String password;
}
