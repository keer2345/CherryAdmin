package com.cherry.system.domain;

import com.cherry.common.flex.base.BaseDO;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;

/**
 * 角色和部门关联 sys_role_dept
 *
 * @author Lion Li
 */

@Data
@Table("sys_role_dept")
public class SysRoleDept extends BaseDO {

    /**
     * 角色ID
     */
    @Id(keyType = KeyType.None)
    private String roleId;

    /**
     * 部门ID
     */
    private String deptId;

}
