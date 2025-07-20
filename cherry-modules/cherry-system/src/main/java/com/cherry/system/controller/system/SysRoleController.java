package com.cherry.system.controller.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.cherry.common.core.domain.R;
import com.cherry.common.flex.core.page.PageQuery;
import com.cherry.common.flex.core.page.TableDataInfo;
import com.cherry.common.log.annotation.Log;
import com.cherry.common.log.enums.BusinessType;
import com.cherry.common.web.core.BaseController;
import com.cherry.system.domain.bo.SysRoleBo;
import com.cherry.system.domain.vo.SysRoleVo;
import com.cherry.system.service.ISysRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 角色信息
 *
 * @author keer2345
 * @date 2025-07-19
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/role")
@Slf4j
public class SysRoleController extends BaseController {
  // todo

  private final ISysRoleService roleService;

  /** 获取角色信息列表 */
  @SaCheckPermission("system:role:list")
  @GetMapping("/list")
  public TableDataInfo<SysRoleVo> list(SysRoleBo role, PageQuery pageQuery) {
    log.info("sys role bo:{}", role);
    return roleService.selectPageRoleList(role, pageQuery);
  }

  /** 状态修改 */
  @SaCheckPermission("system:role:edit")
  @Log(title = "角色管理", businessType = BusinessType.UPDATE)
  @PutMapping("/changeStatus")
  public R<Void> changeStatus(@RequestBody SysRoleBo role) {
    roleService.checkRoleAllowed(role);
    roleService.checkRoleDataScope(role.getId());
    return toAjax(roleService.updateRoleStatus(role.getId(), role.getStatus()));
  }
}
