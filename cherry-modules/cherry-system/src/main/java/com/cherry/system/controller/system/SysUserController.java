package com.cherry.system.controller.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.util.ObjUtil;
import com.cherry.common.core.domain.R;
import com.cherry.common.core.domain.model.LoginUser;
import com.cherry.common.mybatis.core.page.PageQuery;
import com.cherry.common.mybatis.core.page.TableDataInfo;
import com.cherry.common.satoken.utils.LoginHelper;
import com.cherry.common.tenant.helper.TenantHelper;
import com.cherry.common.web.core.BaseController;
import com.cherry.system.domain.bo.SysDeptBo;
import com.cherry.system.domain.bo.SysUserBo;
import com.cherry.system.domain.vo.SysUserVo;
import com.cherry.system.domain.vo.UserInfoVo;
import com.cherry.system.service.ISysDeptService;
import com.cherry.system.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
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

    /**
     * 获取用户列表
     */
    @SaCheckPermission("system:user:list")
    @GetMapping("/list")
    public TableDataInfo<SysUserVo> list(SysUserBo user, PageQuery pageQuery) {
        return userService.selectPageUserList(user, pageQuery);
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

    /**
     * 获取部门树列表
     */
    @SaCheckPermission("system:user:list")
    @GetMapping("/deptTree")
    public R<List<Tree<Long>>> deptTree(SysDeptBo dept) {
        return R.ok(deptService.selectDeptTreeList(dept));
    }
}
