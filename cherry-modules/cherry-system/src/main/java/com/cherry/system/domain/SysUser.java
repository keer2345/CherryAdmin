package com.cherry.system.domain;

import com.cherry.common.core.constant.SystemConstants;
import com.cherry.common.tenant.core.TenantEntity;

import java.time.LocalDateTime;
import java.util.Date;

import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户对象 sys_user
 *
 * @author keer
 * @date 2025-05-26
 */
@Data
@NoArgsConstructor
@Table("sys_user")
public class SysUser extends TenantEntity {
    // todo

    /** 用户ID */
//  @TableId(value = "user_id")
//  private Long userId;

    /**
     * 部门ID
     */
    private String deptId;

    /**
     * 用户账号
     */
    private String userName;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户类型（sys_user系统用户）
     */
    private String userType;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    private String phonenumber;

    /**
     * 用户性别
     */
    private String sex;

    /**
     * 用户头像
     */
    private Long avatar;

    /**
     * 密码
     */
//  @TableField(
//      insertStrategy = FieldStrategy.NOT_EMPTY,
//      updateStrategy = FieldStrategy.NOT_EMPTY,
//      whereStrategy = FieldStrategy.NOT_EMPTY)
    private String password;

    /**
     * 帐号状态（0正常 1停用）
     */
    private String status;


    /**
     * 最后登录IP
     */
    private String loginIp;

    /**
     * 最后登录时间
     */
    private LocalDateTime loginDate;

    /**
     * 备注
     */
    private String remark;

    public SysUser(String userId) {
        super.setId(userId);
    }

    public boolean isSuperAdmin() {
        return SystemConstants.SUPER_ADMIN_ID.equals(super.getId());
    }
}
