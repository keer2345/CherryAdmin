package com.cherry.common.flex.handler;

import com.cherry.common.flex.base.BaseDO;
import com.cherry.common.satoken.utils.LoginHelper;
import com.mybatisflex.annotation.InsertListener;

/**
 * MybatisInsertListener
 *
 * @author keer2345
 * @date 2025-06-24
 */
public class MybatisInsertListener<T extends BaseDO> implements InsertListener {
    //
    // https://gitee.com/wei-yongpe/openlogic/blob/master/openlogic-common/openlogic-common-datasource/src/main/java/com/openlogic/common/datasource/impl/DomainInsertListenerImpl.java

    // todo

    @Override
    public void onInsert(Object entity) {
        //    Object username = StpUtil.getExtra("username"); // 此处获取用户名
        String userId = LoginHelper.getUserId();
        String deptId = LoginHelper.getDeptId();
        T t = (T) entity;
        if (userId != null && entity instanceof BaseDO) {
            //      t.setCreator(SecurityUtil.getUserName());
            t.setCreator(userId);
        }else{
            t.setCreator("");
        }
        if (deptId != null && entity instanceof BaseDO) {
            //      t.setCreator(SecurityUtil.getUserName());
            t.setCreateDept(deptId);
        }else{
            t.setCreateDept("");
        }
    }
}
