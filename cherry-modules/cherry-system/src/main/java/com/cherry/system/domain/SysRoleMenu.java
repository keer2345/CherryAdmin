package com.cherry.system.domain;

import com.cherry.common.flex.base.BaseDO;
import com.mybatisflex.annotation.Table;
import lombok.Data;

/**
 * 角色和菜单关联 sys_role_menu
 *
 * @author keer2345
 * @date 2025-07-02
 */
@Data
@Table("sys_role_menu")
public class SysRoleMenu extends BaseDO {

    /**
     * 角色ID
     */
    private String roleId;

    /**
     * 菜单ID
     */
    private String menuId;
}
