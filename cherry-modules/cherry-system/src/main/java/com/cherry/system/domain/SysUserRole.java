package com.cherry.system.domain;

import com.cherry.common.flex.base.BaseDO;
import com.mybatisflex.annotation.Table;
import lombok.Data;

/**
 * 用户和角色关联 sys_user_role
 *
 * @author keer2345
 * @date 2025-07-02
 */
@Data
@Table("sys_user_role")
public class SysUserRole extends BaseDO {
    /**
     * 用户ID
     */
    private String userId;

    /**
     * 角色ID
     */
    private String roleId;
}
