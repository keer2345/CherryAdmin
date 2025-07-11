package com.cherry.common.flex.handler;

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
  public void onUpdate(Object entity) {
      log.info(">>>>>> 开始修改0 {}",LoginHelper.getLoginUser());
      String userId = LoginHelper.getUserId();
      T t = (T) entity;
//      t.setUpdateTime(new Date());
      t.setUpdateTime(LocalDateTime.now());
      log.info(">>>>>> 开始修改1 {}, {}, {}",LoginHelper.getUserId(),LoginHelper.getUsername(),LoginHelper.getDeptId());
    if (userId != null && entity instanceof BaseDO) {
      //      t.setUpdater(SecurityUtil.getUserName());
      t.setUpdater(userId);
        log.info(">>>>>> 开始修改2 {}, {}, {}",LoginHelper.getUserId(),LoginHelper.getUsername(),LoginHelper.getDeptId());
    }else{
        t.setUpdater("");
        log.info(">>>>>> 开始修改3 {}, {}, {}",LoginHelper.getUserId(),LoginHelper.getUsername(),LoginHelper.getDeptId());
    }
  }
}
