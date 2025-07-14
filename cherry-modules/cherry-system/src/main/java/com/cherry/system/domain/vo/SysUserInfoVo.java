package com.cherry.system.domain.vo;

import java.util.List;
import lombok.Data;

/**
 * 用户信息
 *
 * @author Michelle.Chung
 */
@Data
public class SysUserInfoVo {

    /**
     * 用户信息
     */
    private SysUserVo user;

    /**
     * 角色ID列表
     */
    private List<String> roleIds;

    /**
     * 角色列表
     */
    private List<SysRoleVo> roles;

    /**
     * 岗位ID列表
     */
    private List<String> postIds;

    /**
     * 岗位列表
     */
    private List<SysPostVo> posts;

}
