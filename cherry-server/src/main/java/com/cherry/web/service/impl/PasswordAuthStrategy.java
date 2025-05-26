package com.cherry.web.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cherry.common.core.constant.Constants;
import com.cherry.common.core.constant.GlobalConstants;
import com.cherry.common.core.constant.SystemConstants;
import com.cherry.common.core.domain.model.PasswordLoginBody;
import com.cherry.common.core.enums.LoginType;
import com.cherry.common.core.exception.user.CaptchaException;
import com.cherry.common.core.exception.user.CaptchaExpireException;
import com.cherry.common.core.exception.user.UserException;
import com.cherry.common.core.utils.MessageUtils;
import com.cherry.common.core.utils.StringUtils;
import com.cherry.common.core.utils.ValidatorUtils;
import com.cherry.common.json.utils.JsonUtils;
import com.cherry.common.redis.utils.RedisUtils;
import com.cherry.common.web.config.properties.CaptchaProperties;
import com.cherry.system.domain.SysUser;
import com.cherry.system.domain.vo.SysClientVo;
import com.cherry.system.domain.vo.SysUserVo;
import com.cherry.system.mapper.SysUserMapper;
import com.cherry.web.domain.vo.LoginVo;
import com.cherry.web.service.IAuthStrategy;
import com.cherry.web.service.SysLoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 密码认证策略
 *
 * @author keer
 * @date 2025-05-23
 */
@Slf4j
@Service("password" + IAuthStrategy.BASE_NAME)
@RequiredArgsConstructor
public class PasswordAuthStrategy implements IAuthStrategy {
  // todo
  private final CaptchaProperties captchaProperties;
  private final SysLoginService loginService;
  private final SysUserMapper userMapper;

  @Override
  public LoginVo login(String body, SysClientVo client) {
    PasswordLoginBody loginBody = JsonUtils.parseObject(body, PasswordLoginBody.class);
    ValidatorUtils.validate(loginBody);

    String tenantId = loginBody.getTenantId();
    String username = loginBody.getUsername();
    String password = loginBody.getPassword();
    String code = loginBody.getCode();
    String uuid = loginBody.getUuid();

    boolean captchaEnabled = captchaProperties.getEnable();
    // 验证码开关
    if (captchaEnabled) {
      validateCaptcha(tenantId, username, code, uuid);
    }

    SysUserVo user = loadUserByUsername(username);
    loginService.checkLogin(
        LoginType.PASSWORD,
        tenantId,
        username,
        () -> !BCrypt.checkpw(password, user.getPassword()));

    return null;
  }

  /**
   * 校验验证码
   *
   * @param username 用户名
   * @param code 验证码
   * @param uuid 唯一标识
   */
  private void validateCaptcha(String tenantId, String username, String code, String uuid) {
    String verifyKey = GlobalConstants.CAPTCHA_CODE_KEY + StringUtils.blankToDefault(uuid, "");
    String captcha = RedisUtils.getCacheObject(verifyKey);
    RedisUtils.deleteObject(verifyKey);
    if (captcha == null) {
      loginService.recordLogininfor(
          tenantId, username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire"));
      throw new CaptchaExpireException();
    }
    if (!code.equalsIgnoreCase(captcha)) {
      loginService.recordLogininfor(
          tenantId, username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error"));
      throw new CaptchaException();
    }
  }

  private SysUserVo loadUserByUsername(String username) {
    SysUserVo user =
        userMapper.selectVoOne(
            new LambdaQueryWrapper<SysUser>().eq(SysUser::getUserName, username));
    if (ObjUtil.isNull(user)) {
      log.info("登录用户：{} 不存在.", username);
      throw new UserException("user.not.exists", username);
    } else if (SystemConstants.DISABLE.equals(user.getStatus())) {
      log.info("登录用户：{} 已被停用.", username);
      throw new UserException("user.blocked", username);
    }
    return user;
  }
}
