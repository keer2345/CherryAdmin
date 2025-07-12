package com.cherry.common.flex.handler;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.http.HttpStatus;
import com.cherry.common.core.domain.model.LoginUser;
import com.cherry.common.core.exception.ServiceException;
import com.cherry.common.flex.base.BaseDO;
import com.cherry.common.satoken.utils.LoginHelper;
import com.mybatisflex.annotation.UpdateListener;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * MybatisUpdateListener
 *
 * @author keer2345
 * @date 2025-06-24
 */
@Slf4j
public class MybatisUpdateListener<T extends BaseDO> implements UpdateListener {

  //
  // https://gitee.com/wei-yongpe/openlogic/blob/master/openlogic-common/openlogic-common-datasource/src/main/java/com/openlogic/common/datasource/impl/DomainUpdateListenerImpl.java

  // todo
  @Override
  public void onUpdate(Object o) {

    try {
      if (ObjUtil.isNotNull(o) && o instanceof BaseDO entity) {
        entity.setUpdateTime(LocalDateTime.now());
        LoginUser loginUser = getLoginUser();
        log.info(">>>>>> 开始修改0 {}", loginUser);

        log.info(
            ">>>>>> 开始修改1 {}, {}, {}",
            LoginHelper.getUserId(),
            LoginHelper.getUsername(),
            LoginHelper.getDeptId());
        if (ObjUtil.isNotNull(loginUser)) {
          entity.setUpdater(loginUser.getUserId());
        }
      }
    } catch (Exception e) {
      throw new ServiceException(
          "Update监听自动注入异常 => " + e.getMessage(), HttpStatus.HTTP_UNAUTHORIZED);
    }
  }

  private LoginUser getLoginUser() {
    LoginUser loginUser;
    try {
      loginUser = LoginHelper.getLoginUser();
    } catch (Exception e) {
      log.warn("Update自动注入警告 =》 用户未登录");
      return null;
    }
    return loginUser;
  }
}
