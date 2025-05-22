package com.cherry.common.tenant.helper;

import cn.hutool.core.convert.Convert;
import com.cherry.common.core.utils.SpringUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 租户助手
 *
 * @author keer
 * @date 2025-05-22
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TenantHelper {
  // todo

  /** 租户功能是否启用 */
  public static boolean isEnable() {
    return Convert.toBool(SpringUtils.getProperty("tenant.enable"), false);
  }
}
