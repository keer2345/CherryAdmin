package com.cherry.web.controller;

import cn.hutool.core.util.ObjUtil;
import com.cherry.common.core.constant.SystemConstants;
import com.cherry.common.core.domain.LoginBody;
import com.cherry.common.core.domain.R;
import com.cherry.common.core.utils.MessageUtils;
import com.cherry.common.core.utils.StringUtils;
import com.cherry.common.core.utils.ValidatorUtils;
import com.cherry.common.json.utils.JsonUtils;
import com.cherry.common.tenant.helper.TenantHelper;
import com.cherry.system.domain.vo.SysClientVo;
import com.cherry.web.domain.vo.LoginTenantVo;
import com.cherry.web.domain.vo.LoginVo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 认证
 *
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
  // @ApiEncrypt
  @PostMapping("/login")
  public R<LoginVo> login(@RequestBody String body) {
    log.info("t1: {}", body);
    LoginBody loginBody = JsonUtils.parseObject(body, LoginBody.class);
    log.info("t2: {}", loginBody.toString());
    ValidatorUtils.validate(loginBody);

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

  /**
   * 登录页面租户下拉框
   *
   * @return 租户列表
   */
  @GetMapping("/tenant/list")
  public R<LoginTenantVo> tenantList(HttpServletRequest request) throws Exception {

      // 返回对象
      LoginTenantVo result = new LoginTenantVo();
      boolean enable = TenantHelper.isEnable();
      result.setTenantEnabled(enable);
      // 如果未开启租户这直接返回
      if (!enable) {
          return R.ok(result);
      }
  }
}
