package com.cherry.system.controller.system;

import com.cherry.common.core.domain.R;
import com.cherry.common.satoken.utils.LoginHelper;
import com.cherry.common.web.core.BaseController;
import com.cherry.system.domain.vo.ProfileVo;
import com.cherry.system.domain.vo.SysUserVo;
import com.cherry.system.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author keer
 * @date 2025-06-06
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/user/profile")
public class SysProfileController extends BaseController {
  // todo

    private final ISysUserService userService;

    /**
     * 个人信息
     */
    @GetMapping
    public R<ProfileVo> profile() {
        SysUserVo user =userService.selectUserById(LoginHelper.getUserId());
        ProfileVo profileVo=new ProfileVo();
        profileVo.setUser(user);
        profileVo.setRoleGroup(userService.selectUserRoleGroup(user.getUserId()));
        profileVo.setPostGroup(userService.selectUserPostGroup(user.getUserId()));
        return R.ok(profileVo);
    }
}
