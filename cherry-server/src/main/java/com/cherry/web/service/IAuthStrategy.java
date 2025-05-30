package com.cherry.web.service;

import com.cherry.common.core.exception.ServiceException;
import com.cherry.common.core.utils.SpringUtils;
import com.cherry.system.domain.vo.SysClientVo;
import com.cherry.web.domain.vo.LoginVo;

/**
 * 授权策略
 *
 * @author keer
 * @date 2025-05-23
 */
public interface IAuthStrategy {
  // todo
  String BASE_NAME = "AuthStrategy";

  /**
   * 登录
   *
   * @param body 登录对象
   * @param client 授权管理视图对象
   * @param grantType 授权类型
   * @return 登录验证信息
   */
  static LoginVo login(String body, SysClientVo client, String grantType) {
      // 授权类型和客户端id
    String beanName = grantType + BASE_NAME;
    if (!SpringUtils.containsBean(beanName)) {
      throw new ServiceException("授权类型不正确!");
    }
    IAuthStrategy instance = SpringUtils.getBean(beanName);
    return instance.login(body, client);
  }

  /**
   * 登录
   *
   * @param body 登录对象
   * @param client 授权管理视图对象
   * @return 登录验证信息
   */
  LoginVo login(String body, SysClientVo client);
}
