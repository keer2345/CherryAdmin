package com.cherry.system.domain;

import com.cherry.common.flex.base.BaseDO;
import com.mybatisflex.annotation.Table;
import lombok.Data;

/**
 * 角色和数据权限关联 sys_role_perm
 *
 * @author keer2345
 * @date 2025-07-02
 */
@Data
@Table("sys_role_perm")
public class SysRolePerm extends BaseDO {

    /**
     * 角色ID
     */
    private String roleId;

    /**
     * 菜单ID
     */
    private String permissionId;
}
