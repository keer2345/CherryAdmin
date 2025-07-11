package com.cherry.common.flex.handler;

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
    // https://gitee.com/wei-yongpe/openlogic/blob/master/openlogic-common/openlogic-common-datasource/src/main/java/com/openlogic/common/datasource/impl/DomainInsertListenerImpl.java

    // todo

    @Override
    public void onInsert(Object entity) {
//        https://gitee.com/dromara/sa-token/issues/IC4XFE
        log.info(">>>>>> 开始新建0 {}",LoginHelper.getLoginUser());
        //    Object username = StpUtil.getExtra("username"); // 此处获取用户名
        String userId = LoginHelper.getUserId();
        String deptId = LoginHelper.getDeptId();
        T t = (T) entity;

//        t.setCreateTime(new Date());
//        t.setUpdateTime(new Date());

        LocalDateTime now = LocalDateTime.now();
        t.setCreateTime(now);
        t.setUpdateTime(now);


        log.info(">>>>>> 开始新建1 {}, {}, {}",LoginHelper.getUserId(),LoginHelper.getUsername(),LoginHelper.getDeptId());

        if (userId != null && entity instanceof BaseDO) {
            log.info(">>>>>> 开始新建2 {}, {}, {}",LoginHelper.getUserId(),LoginHelper.getUsername(),LoginHelper.getDeptId());
            t.setCreator(userId);
        }else{
            log.info(">>>>>> 开始新建3 {}, {}, {}",LoginHelper.getUserId(),LoginHelper.getUsername(),LoginHelper.getDeptId());
            t.setCreator("");
        }
        if (deptId != null && entity instanceof BaseDO) {
            log.info(">>>>>> 开始新建4 {}, {}, {}",LoginHelper.getUserId(),LoginHelper.getUsername(),LoginHelper.getDeptId());
            t.setCreateDept(deptId);
        }else{
            log.info(">>>>>> 开始新建5 {}, {}, {}",LoginHelper.getUserId(),LoginHelper.getUsername(),LoginHelper.getDeptId());
            t.setCreateDept("");
        }
    }
}
