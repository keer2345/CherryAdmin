package com.cherry.common.flex.helper;

import cn.hutool.core.convert.Convert;
import lombok.extern.slf4j.Slf4j;

/**
 * 数据库助手
 *
 * @author keer2345
 * @date 2025-07-14
 */
@Slf4j
public class DataBaseHelper {
  // todo

  /** 获取当前数据库类型 */
  public static String getDataBaseType() {
    return "postgresql";
  }

  public static String findInSet(Object var1, String var2) {

    String var = Convert.toStr(var1);

    //    if (dataBasyType == DataBaseType.SQL_SERVER) {
    if (getDataBaseType() == "sqlserver") {
      // charindex(',100,' , ',0,100,101,') <> 0
      return "charindex(',%s,' , ','+%s+',') <> 0".formatted(var, var2);
    } else
    //        if (dataBasyType == DataBaseType.POSTGRE_SQL) {
    if (getDataBaseType() == "postgresql") {
      // (select strpos(',0,100,101,' , ',100,')) <> 0
      return "(select strpos(','||%s||',' , ',%s,')) <> 0".formatted(var2, var);
    } else
    //        if (dataBasyType == DataBaseType.ORACLE) {
    if (getDataBaseType() == "oracle") {
      // instr(',0,100,101,' , ',100,') <> 0
      return "instr(','||%s||',' , ',%s,') <> 0".formatted(var2, var);
    }
    // find_in_set(100 , '0,100,101')
    return "find_in_set('%s' , %s) <> 0".formatted(var, var2);
  }
}
