package com.cherry.system.controller.system;

import com.cherry.common.core.domain.R;
import com.cherry.common.satoken.utils.LoginHelper;
import com.cherry.common.web.core.BaseController;
import com.cherry.system.domain.vo.SysSocialVo;
import com.cherry.system.service.ISysSocialService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 社会化关系
 *
 * @author keer
 * @date 2025-06-06
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/social")
public class SysSocialController extends BaseController {

  private final ISysSocialService socialUserService;

  /** 查询社会化关系列表 */
  @GetMapping("/list")
  public R<List<SysSocialVo>> list() {
    return R.ok(socialUserService.queryListByUserId(LoginHelper.getUserId()));
  }
}
