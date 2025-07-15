package com.cherry.common.flex.permission;

import com.cherry.common.core.utils.SpringUtils;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据权限组
 *
 * @author keer2345
 * @date 2025-07-15
 */
@AllArgsConstructor
@Getter
public class DataPermission {

  private static final PlusDataPermissionHandler DATA_PERMISSION_HANDLER =
      SpringUtils.getBean(PlusDataPermissionHandler.class);

  DataColumn[] value;

  public static DataPermission of(DataColumn... value) {
    return new DataPermission(value);
  }

  public void handler(QueryWrapper queryWrapper) {
    DATA_PERMISSION_HANDLER.handlerDataPermission(this, queryWrapper, true);
  }

  public String toSQL(boolean isSelect) {
    return DATA_PERMISSION_HANDLER.getSQL(this, isSelect);
  }

  public String toSQL() {
    return toSQL(true);
  }
}
