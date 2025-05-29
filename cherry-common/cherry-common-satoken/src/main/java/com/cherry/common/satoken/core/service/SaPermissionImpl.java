package com.cherry.common.satoken.core.service;

import cn.dev33.satoken.stp.StpInterface;
import com.cherry.common.core.domain.model.LoginUser;
import com.cherry.common.core.enums.UserType;
import com.cherry.common.satoken.handler.LoginHelper;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * sa-token 权限管理实现类
 *
 * @author keer
 * @date 2025-05-29
 */
@Slf4j
public class SaPermissionImpl implements StpInterface {

  /** 获取菜单权限列表 */
  @Override
  public List<String> getPermissionList(Object o, String s) {
    LoginUser loginUser = LoginHelper.getLoginUser();
      log.info("r2 loginUser: {}",loginUser);
    UserType userType = UserType.getUserType(loginUser.getUserType());

    if (userType == UserType.APP_USER.SYS_USER) {
      return new ArrayList<>(loginUser.getMenuPermission());
    } else if (userType == UserType.APP_USER) {
      // 其他端 自行根据业务编写
    }
    return new ArrayList<>();
  }

  /** 获取角色权限列表 */
  @Override
  public List<String> getRoleList(Object o, String s) {
    LoginUser loginUser = LoginHelper.getLoginUser();
    UserType userType = UserType.getUserType(loginUser.getUserType());
    if (userType == UserType.SYS_USER) {
      return new ArrayList<>(loginUser.getRolePermission());
    } else if (userType == UserType.APP_USER) {
      // 其他端 自行根据业务编写
    }
    return new ArrayList<>();
  }
}
