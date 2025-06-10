package com.cherry.system.controller.system;

import com.cherry.common.core.domain.R;
import com.cherry.common.web.core.BaseController;
import com.cherry.system.service.ISysConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 参数配置 信息操作处理
 *
 * @author keer
 * @date 2025-06-10
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/config")
public class SysConfigController extends BaseController {
  // todo

    private final ISysConfigService configService;
  /**
   * 根据参数键名查询参数值
   *
   * @param configKey 参数Key
   */
  @GetMapping(value = "/configKey/{configKey}")
  public R<String> getConfigKey(@PathVariable String configKey) {
    return R.ok("操作成功", configService.selectConfigByKey(configKey));
  }
}
