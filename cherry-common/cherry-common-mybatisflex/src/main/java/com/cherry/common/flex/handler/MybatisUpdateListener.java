package com.cherry.common.flex.handler;

import com.cherry.common.flex.base.BaseDO;
import com.cherry.common.satoken.utils.LoginHelper;
import com.mybatisflex.annotation.UpdateListener;

/**
 * MybatisUpdateListener
 *
 * @author keer2345
 * @date 2025-06-24
 */
public class MybatisUpdateListener<T extends BaseDO> implements UpdateListener {

  //
  // https://gitee.com/wei-yongpe/openlogic/blob/master/openlogic-common/openlogic-common-datasource/src/main/java/com/openlogic/common/datasource/impl/DomainUpdateListenerImpl.java

  // todo
  @Override
  public void onUpdate(Object entity) {
      String userId = LoginHelper.getUserId();
    if (userId != null && entity instanceof BaseDO) {
      T t = (T) entity;
      //      t.setUpdater(SecurityUtil.getUserName());
      t.setUpdater(userId);
      //      t.setUpdateTime(new Date());
    }
  }
}
