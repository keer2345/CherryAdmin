package com.cherry.common.flex.config;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.http.HttpStatus;
import com.cherry.common.core.domain.model.LoginUser;
import com.cherry.common.core.exception.ServiceException;
import com.cherry.common.flex.base.BaseDO;
import com.cherry.common.flex.handler.MybatisInsertListener;
import com.cherry.common.flex.handler.MybatisUpdateListener;
import com.cherry.common.satoken.utils.LoginHelper;
import com.mybatisflex.annotation.UpdateListener;
import com.mybatisflex.core.FlexGlobalConfig;
import com.mybatisflex.spring.boot.MybatisFlexAutoConfiguration;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;

/**
 * MyBaits 配置类
 *
 * @author keer2345
 * @date 2025-06-24
 */
@AutoConfiguration(before = MybatisFlexAutoConfiguration.class)
@MapperScan(value = "${cherry.info.base-package}.**.mapper", annotationClass = Mapper.class)
public class CherryMybatisAutoConfiguration {
  // todo

  // https://gitee.com/wei-yongpe/openlogic/blob/master/openlogic-common/openlogic-common-datasource/src/main/java/com/openlogic/common/datasource/config/MybatisFlexConfig.java

  public CherryMybatisAutoConfiguration() {
    globalConfig();
  }

  /** 全局配置 */
  private void globalConfig() {
    FlexGlobalConfig globalConfig = FlexGlobalConfig.getDefaultConfig();

    // 注册 insertListener
    globalConfig.registerInsertListener(new MybatisInsertListener<>(), BaseDO.class);
    // 注册 updateListener
    globalConfig.registerUpdateListener(new MybatisUpdateListener<>(), BaseDO.class);

    //    https://mybatis-flex.com/zh/core/logic-delete.html
    // 设置数据库正常时的值
    globalConfig.setNormalValueOfLogicDelete(false);
    // 设置数据已被删除时的值
    globalConfig.setDeletedValueOfLogicDelete(true);
  }
}
