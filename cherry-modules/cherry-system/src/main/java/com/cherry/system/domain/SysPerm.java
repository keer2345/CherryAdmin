package com.cherry.system.domain;

import com.cherry.common.flex.base.BaseDO;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据权限表
 *
 * @author keer2345
 * @date 2025-07-17
 */
@Data
@NoArgsConstructor
@Table("sys_perm")
public class SysPerm extends BaseDO {
  // todo

    /**
     * 角色名称
     */
    private String permissionName;
    /**
     * 角色状态（0正常 1停用）
     */
    private String status;
}
