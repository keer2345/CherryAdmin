package com.cherry.system.controller.system;

import cn.dev33.satoken.secure.BCrypt;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.cherry.common.core.constant.CacheNames;
import com.cherry.common.core.domain.R;
import com.cherry.common.core.utils.StringUtils;
import com.cherry.common.log.annotation.Log;
import com.cherry.common.log.enums.BusinessType;
import com.cherry.common.mybatis.helper.DataPermissionHelper;
import com.cherry.common.satoken.utils.LoginHelper;
import com.cherry.common.web.core.BaseController;
import com.cherry.system.domain.SysUser;
import com.cherry.system.domain.bo.SysUserBo;
import com.cherry.system.domain.bo.SysUserPasswordBo;
import com.cherry.system.domain.bo.SysUserProfileBo;
import com.cherry.system.domain.vo.ProfileVo;
import com.cherry.system.domain.vo.SysUserVo;
import com.cherry.system.mapper.SysUserMapper;
import com.cherry.system.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    private final SysUserMapper baseMapper;
  private final ISysUserService userService;


  /** 个人信息 */
  @GetMapping
  public R<ProfileVo> profile() {
    SysUserVo user = userService.selectUserById(LoginHelper.getUserId());
    ProfileVo profileVo = new ProfileVo();
    profileVo.setUser(user);
    profileVo.setRoleGroup(userService.selectUserRoleGroup(user.getUserId()));
    profileVo.setPostGroup(userService.selectUserPostGroup(user.getUserId()));
    return R.ok(profileVo);
  }

  /** 修改用户信息 */
  // @RepeatSubmit
  @Log(title = "个人信息", businessType = BusinessType.UPDATE)
  @PutMapping
  public R<Void> updateProfile(@Validated @RequestBody SysUserProfileBo profile) {
    SysUserBo user = BeanUtil.toBean(profile, SysUserBo.class);
    user.setUserId(LoginHelper.getUserId());
    String username = LoginHelper.getUsername();
    if (StringUtils.isNotEmpty(user.getPhonenumber()) && !userService.checkPhoneUnique(user)) {
      return R.fail("修改用户'" + username + "'失败，手机号码已存在");
    }
    if (StringUtils.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(user)) {
      return R.fail("修改用户'" + username + "'失败，邮箱账号已存在");
    }
    int rows = DataPermissionHelper.ignore(() -> userService.updateUserProfile(user));
    if (rows > 0) {
      return R.ok();
    }
    return R.fail("修改个人信息异常，请联系管理员");
  }

    /**
     * 重置密码
     *
     * @param bo 新旧密码
     */
    // @RepeatSubmit
    // @ApiEncrypt
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping("/updatePwd")
    public R<Void> updatePwd(@Validated @RequestBody SysUserPasswordBo bo) {
        SysUserVo user = userService.selectUserById(LoginHelper.getUserId());
        String password = user.getPassword();
        if (!BCrypt.checkpw(bo.getOldPassword(), password)) {
            return R.fail("修改密码失败，旧密码错误");
        }
        if (BCrypt.checkpw(bo.getNewPassword(), password)) {
            return R.fail("新密码不能与旧密码相同");
        }
        int rows = DataPermissionHelper.ignore(() -> userService.resetUserPwd(user.getUserId(), BCrypt.hashpw(bo.getNewPassword())));
        if (rows > 0) {
            return R.ok();
        }
        return R.fail("修改密码异常，请联系管理员");
    }

}
