package com.cherry.common.tenant.core;

import com.cherry.common.mybatis.core.domain.BaseEntity;
import lombok.Data;

/**
 * 租户基类
 *
 * @author keer
 * @date 2025-05-26
 */
@Data
public class TenantEntity extends BaseEntity {
  /** 租户编号 */
  private String tenantId;
}
