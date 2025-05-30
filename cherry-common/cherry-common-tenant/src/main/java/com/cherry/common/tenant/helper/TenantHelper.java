package com.cherry.common.tenant.helper;

import cn.dev33.satoken.context.SaHolder;
import cn.hutool.core.convert.Convert;
import com.cherry.common.core.constant.GlobalConstants;
import com.cherry.common.core.utils.SpringUtils;
import com.cherry.common.redis.utils.RedisUtils;
import com.cherry.common.satoken.utils.LoginHelper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

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
  private static final String DYNAMIC_TENANT_KEY =
      GlobalConstants.GLOBAL_REDIS_KEY + "dynamicTenant";
  private static final ThreadLocal<String> TEMP_DYNAMIC_TENANT = new ThreadLocal<>();

  /** 租户功能是否启用 */
  public static boolean isEnable() {
    return Convert.toBool(SpringUtils.getProperty("tenant.enable"), false);
  }

  public static void setDynamic(String tenantId) {
    setDynamic(tenantId, false);
  }

  /**
   * 设置动态租户(一直有效 需要手动清理)
   *
   * <p>如果为未登录状态下 那么只在当前线程内生效
   *
   * @param tenantId 租户id
   * @param global 是否全局生效
   */
  public static void setDynamic(String tenantId, boolean global) {
    if (!isEnable()) {
      return;
    }
    if (!LoginHelper.isLogin() || !global) {
      TEMP_DYNAMIC_TENANT.set(tenantId);
      return;
    }
    String cacheKey = DYNAMIC_TENANT_KEY + ":" + LoginHelper.getUserId();
    RedisUtils.setCacheObject(cacheKey, tenantId);
    SaHolder.getStorage().set(cacheKey, tenantId);
  }

  /** 清除动态租户 */
  public static void clearDynamic() {
    if (!isEnable()) {
      return;
    }
    if (!LoginHelper.isLogin()) {
      TEMP_DYNAMIC_TENANT.remove();
      return;
    }
    TEMP_DYNAMIC_TENANT.remove();
    String cacheKey = DYNAMIC_TENANT_KEY + ":" + LoginHelper.getUserId();
    RedisUtils.deleteObject(cacheKey);
    SaHolder.getStorage().delete(cacheKey);
  }

  /**
   * 在动态租户中执行
   *
   * @param handle 处理执行方法
   */
  public static <T> T dynamic(String tenantId, Supplier<T> handle) {
    setDynamic(tenantId);
    try {
      return handle.get();
    } finally {
      clearDynamic();
    }
  }
}
