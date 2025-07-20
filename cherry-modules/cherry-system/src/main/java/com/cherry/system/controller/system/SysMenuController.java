package com.cherry.system.controller.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import com.cherry.common.core.constant.TenantConstants;
import com.cherry.common.core.domain.R;
import com.cherry.common.satoken.utils.LoginHelper;
import com.cherry.common.web.core.BaseController;
import com.cherry.system.domain.SysMenu;
import com.cherry.system.domain.bo.SysMenuBo;
import com.cherry.system.domain.vo.RouterVo;
import com.cherry.system.domain.vo.SysMenuVo;
import com.cherry.system.service.ISysMenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 菜单信息
 *
 * @author keer
 * @date 2025-05-28
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/menu")
public class SysMenuController extends BaseController {
  // todo
  private final ISysMenuService menuService;

  /**
   * 获取路由信息
   *
   * @return 路由信息
   */
  @GetMapping("/getRouters")
  public R<List<RouterVo>> getRouters() {
    List<SysMenu> menus = menuService.selectMenuTreeByUserId(LoginHelper.getUserId());
    return R.ok(menuService.buildMenus(menus));
  }

  /** 获取菜单列表 */
//  @SaCheckRole(
//      value = {TenantConstants.SUPER_ADMIN_ROLE_KEY, TenantConstants.TENANT_ADMIN_ROLE_KEY},
//      mode = SaMode.OR)
//  @SaCheckPermission("system:menu:list")
  @GetMapping("/list")
  public R<List<SysMenuVo>> list(SysMenuBo menu) {
    List<SysMenuVo> menus = menuService.selectMenuList(menu, LoginHelper.getUserId());
    return R.ok(menus);
  }
}
