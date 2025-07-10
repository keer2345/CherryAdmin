package com.cherry.system.domain;

import com.cherry.common.tenant.core.TenantEntity;
import com.mybatisflex.annotation.Table;
import lombok.Data;

/**
 * SysDictType
 *
 * @author keer2345
 * @date 2025-07-02
 */
@Data
@Table("sys_dict_type")
public class SysDictType extends TenantEntity {

  /** 字典主键 */
  //    @TableId(value = "dict_id")
  //    private Long dictId;

  /** 字典名称 */
  private String dictName;

  /** 字典类型 */
  private String dictType;

  /** 备注 */
  private String remark;
}
