package com.cherry.system.controller.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import com.cherry.common.core.constant.SystemConstants;
import com.cherry.common.core.domain.R;
import com.cherry.common.core.domain.model.LoginUser;
import com.cherry.common.core.utils.StreamUtils;
import com.cherry.common.flex.core.page.PageQuery;
import com.cherry.common.flex.core.page.TableDataInfo;
import com.cherry.common.satoken.utils.LoginHelper;
import com.cherry.common.tenant.helper.TenantHelper;
import com.cherry.common.web.core.BaseController;
import com.cherry.system.domain.bo.SysDeptBo;
import com.cherry.system.domain.bo.SysRoleBo;
import com.cherry.system.domain.bo.SysUserBo;
import com.cherry.system.domain.vo.SysRoleVo;
import com.cherry.system.domain.vo.SysUserInfoVo;
import com.cherry.system.domain.vo.SysUserVo;
import com.cherry.system.domain.vo.UserInfoVo;
import com.cherry.system.service.ISysDeptService;
import com.cherry.system.service.ISysRoleService;
import com.cherry.system.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户信息
 *
 * @author keer
 * @date 2025-05-28
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/user")
public class SysUserController extends BaseController {
  // todo
  private final ISysUserService userService;
  private final ISysDeptService deptService;
  private final ISysRoleService roleService;

  /** 获取用户列表 */
  @SaCheckPermission("system:user:list")
  @GetMapping("/list")
  public TableDataInfo<SysUserVo> list(SysUserBo user, PageQuery pageQuery) {
    log.info("Sysuserbo: {}", user);
    log.info("Pagequery: {}", pageQuery);
    log.info("查询用户 begin");
      TableDataInfo<SysUserVo> list   =userService.selectPageUserList(user, pageQuery);
      log.info("查询用户 end");
      return list;
  }

  /**
   * 获取用户信息
   *
   * @return 用户信息
   */
  @GetMapping("/getInfo")
  public R<UserInfoVo> getInfo() {
    UserInfoVo userInfoVo = new UserInfoVo();
    LoginUser loginUser = LoginHelper.getLoginUser();

    if (TenantHelper.isEnable() && LoginHelper.isSuperAdmin()) {
      // 超级管理员 如果重新加载用户信息需清除动态租户
      TenantHelper.clearDynamic();
    }
    SysUserVo user = userService.selectUserById(loginUser.getUserId());
    if (ObjUtil.isNull(user)) {
      return R.fail("没有权限访问用户数据!");
    }
    userInfoVo.setUser(user);
    userInfoVo.setPermissions(loginUser.getMenuPermission());
    userInfoVo.setRoles(loginUser.getRolePermission());

    return R.ok(userInfoVo);
  }

  /** 获取部门树列表 */
  @SaCheckPermission("system:user:list")
  @GetMapping("/deptTree")
  public R<List<Tree<String>>> deptTree(SysDeptBo dept) {
    return R.ok(deptService.selectDeptTreeList(dept));
  }

  /**
   * 根据用户编号获取详细信息
   *
   * @param userId 用户ID
   */
  @SaCheckPermission("system:user:query")
  @GetMapping(value = {"/", "/{userId}"})
  public R<SysUserInfoVo> getInfo(@PathVariable(value = "userId", required = false) String userId) {
    SysUserInfoVo userInfoVo = new SysUserInfoVo();
    if (ObjectUtil.isNotNull(userId)) {
//                  userService.checkUserDataScope(userId);
//                  SysUserVo sysUser = userService.selectUserById(userId);
//                  userInfoVo.setUser(sysUser);
//                  userInfoVo.setRoleIds(roleService.selectRoleListByUserId(userId));
//                  Long deptId = sysUser.getDeptId();
//                  if (ObjectUtil.isNotNull(deptId)) {
//                      SysPostBo postBo = new SysPostBo();
//                      postBo.setDeptId(deptId);
//                      userInfoVo.setPosts(postService.selectPostList(postBo));
//                      userInfoVo.setPostIds(postService.selectPostListByUserId(userId));
//                  }
    }

    SysRoleBo roleBo = new SysRoleBo();
    roleBo.setStatus(SystemConstants.NORMAL);
    List<SysRoleVo> roles = roleService.selectRoleList(roleBo);
    userInfoVo.setRoles(
        LoginHelper.isSuperAdmin(userId)
            ? roles
            : StreamUtils.filter(roles, r -> !r.isSuperAdmin()));
    return R.ok(userInfoVo);
  }
}
