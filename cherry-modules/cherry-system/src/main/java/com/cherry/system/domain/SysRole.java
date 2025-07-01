package com.cherry.system.domain;

import com.cherry.common.tenant.core.TenantEntity;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 角色表 sys_role
 *
 * @author keer
 * @date 2025-05-27
 */
@Data
@NoArgsConstructor
@Table("sys_role")
public class SysRole extends TenantEntity {

    /** 角色ID */
//  @TableId(value = "role_id")
//  private Long roleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色权限
     */
    private String roleKey;

    /**
     * 角色排序
     */
    private Integer roleSort;

    /**
     * 数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限 5：仅本人数据权限 6：部门及以下或本人数据权限）
     */
    private String dataScope;

    /**
     * 菜单树选择项是否关联显示（ 0：父子不互相关联显示 1：父子互相关联显示）
     */
    private Boolean menuCheckStrictly;

    /**
     * 部门树选择项是否关联显示（0：父子不互相关联显示 1：父子互相关联显示 ）
     */
    private Boolean deptCheckStrictly;

    /**
     * 角色状态（0正常 1停用）
     */
    private String status;


    /**
     * 备注
     */
    private String remark;

    public SysRole(String id) {
        super.setId(id);
    }
}
