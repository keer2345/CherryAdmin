package com.cherry.common.tenant.core;

import com.cherry.common.flex.base.BaseDO;
import lombok.Data;

/**
 * 租户基类
 *
 * @author keer
 * @date 2025-05-26
 */
@Data
public class TenantEntity extends BaseDO {
  /** 租户编号 */
  private String tenantId;
}
