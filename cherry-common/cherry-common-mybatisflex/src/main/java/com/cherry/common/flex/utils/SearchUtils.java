package com.cherry.common.flex.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.cherry.common.core.utils.DateUtilsPro;
import com.cherry.common.flex.permission.DataColumn;
import com.cherry.common.flex.permission.DataPermission;
import com.mybatisflex.core.query.QueryWrapper;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * SearchUtils
 *
 * @author keer2345
 * @date 2025-07-14
 */
public class SearchUtils {

  /**
   * 将 String 转换成 LocalDateTime 的开始时刻 （beginTime)
   *
   * @param obj Object
   * @return LocalDateTime
   */
  public static LocalDateTime strToDayStart(Object obj) {
    if (obj == null) {
      return null;
    }
    String str = StrUtil.toString(obj);
    Date date = DateUtil.parseDate(str);
    return LocalDateTime.of(DateUtilsPro.dateToLocaldate(date), LocalTime.MIDNIGHT);
  }

  /**
   * 将 String 转换成 LocalDateTime 的最后时刻 （endTime)
   *
   * @param obj Object
   * @return LocalDateTime
   */
  public static Object strToDayEnd(Object obj) {
    if (obj == null) {
      return null;
    }
    String str = StrUtil.toString(obj);
    Date date = DateUtil.parseDate(str);
    return DateUtilsPro.of(date).with(LocalTime.MAX);
  }

  public static QueryWrapper buildTimeBetween(
      QueryWrapper queryWrapper, String columnName, Object beginTime, Object endTime) {
    if (beginTime != null && endTime != null) {
      return queryWrapper.between(columnName, strToDayStart(beginTime), strToDayEnd(endTime));
    } else return queryWrapper;
  }

  /**
   * 数据权限
   *
   * @param queryWrapper
   */
  public static void getQueryDataScope(QueryWrapper queryWrapper, DataColumn... columns) {
    DataPermission dataPermission = DataPermission.of(columns);
    dataPermission.handler(queryWrapper);
  }
  //  public static void getQueryDataScope(QueryWrapper queryWrapper) {
  //      DataColumn[] columns = {DataColumn.of("deptName", "dept_id"), DataColumn.of("userName",
  // "id")};
  //      DataPermission dataPermission = DataPermission.of(columns);
  //      dataPermission.handler(queryWrapper);
  //  }
}
