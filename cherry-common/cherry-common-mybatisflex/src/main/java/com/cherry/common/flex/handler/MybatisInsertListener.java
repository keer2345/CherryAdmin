package com.cherry.common.flex.handler;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.http.HttpStatus;
import com.cherry.common.core.domain.model.LoginUser;
import com.cherry.common.core.exception.ServiceException;
import com.cherry.common.flex.base.BaseDO;
import com.cherry.common.satoken.utils.LoginHelper;
import com.mybatisflex.annotation.InsertListener;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;

/**
 * MybatisInsertListener
 *
 * @author keer2345
 * @date 2025-06-24
 */
@Slf4j
public class MybatisInsertListener<T extends BaseDO> implements InsertListener {
  //
  //        https://gitee.com/dromara/sa-token/issues/IC4XFE
  // https://gitee.com/wei-yongpe/openlogic/blob/master/openlogic-common/openlogic-common-datasource/src/main/java/com/openlogic/common/datasource/impl/DomainInsertListenerImpl.java

  // todo

  @Override
  public void onInsert(Object o) {

    try {
      if (ObjUtil.isNotNull(o) && o instanceof BaseDO entity) {
        LocalDateTime now = LocalDateTime.now();
        entity.setCreateTime(now);

        LoginUser loginUser = getLoginUser();
        log.info(">>>>>> 开始插入0 {}", loginUser);

        log.info(
            ">>>>>> 开始插入1 {}, {}, {}",
            LoginHelper.getUserId(),
            LoginHelper.getUsername(),
            LoginHelper.getDeptId());
        if (ObjUtil.isNotNull(loginUser)) {
          entity.setCreator(loginUser.getUserId());
          entity.setCreateDept(loginUser.getDeptId());
        }
      }
    } catch (Exception e) {
      throw new ServiceException(
          "Insert监听自动注入异常 => " + e.getMessage(), HttpStatus.HTTP_UNAUTHORIZED);
    }
  }

  private LoginUser getLoginUser() {
    LoginUser loginUser;
    try {
      loginUser = LoginHelper.getLoginUser();
    } catch (Exception e) {
      log.info(e.getMessage());
      log.warn("Insert自动注入警告 =》 用户未登录");
      return null;
    }
    return loginUser;
  }
}
