package com.cherry.web.controller;

import cn.hutool.core.util.ObjUtil;
import com.cherry.common.core.constant.SystemConstants;
import com.cherry.common.core.domain.LoginBody;
import com.cherry.common.core.domain.R;
import com.cherry.common.core.utils.MessageUtils;
import com.cherry.common.core.utils.StringUtils;
import com.cherry.common.core.utils.ValidatorUtils;
import com.cherry.common.json.utils.JsonUtils;
import com.cherry.system.domain.vo.SysClientVo;
import com.cherry.web.domain.vo.LoginVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author keer
 * @date 2025-05-21
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
  // todo

  /**
   * 登录方法
   *
   * @param body
   */
  @PostMapping("/login")
  public R<LoginVo> login(@RequestBody String body) {
    LoginBody loginBody = JsonUtils.parseObject(body, LoginBody.class);
    // ValidatorUtils.validate(loginBody);

    // 授权类型和客户端id
    String clientId = loginBody.getClientId();
    String grantType = loginBody.getGrantType();
    // todo
    SysClientVo client = null;
    // 查询不到 client 或 client 内不包含 grantType
    if (ObjUtil.isNull(client) || StringUtils.contains(client.getGrantType(), grantType)) {
      log.info("客户端id: {} 认证类型：{} 异常!.", clientId, grantType);
        return R.fail(MessageUtils.message("auth.grant.type.error"));
    } else if (!SystemConstants.NORMAL.equals(client.getStatus())) {
      return R.fail(MessageUtils.message("auth.grant.type.blocked"));
    }

    // 校验租户

    // 登录

    LoginVo loginVo = null;

    return R.ok(loginVo);
  }
}
