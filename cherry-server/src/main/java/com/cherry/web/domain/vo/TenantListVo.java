package com.cherry.web.domain.vo;

import lombok.Data;

/**
 * 租户列表
 *
 * @author keer
 * @date 2025-05-22
 */
@Data
// todo
// @AutoMapper(target = SysTenantVo.class)
public class TenantListVo {

  /** 租户编号 */
  private String tenantId;

  /** 企业名称 */
  private String companyName;

  /** 域名 */
  private String domain;
}
