package com.cherry.web.controller;

import com.cherry.common.core.utils.SpringUtils;
import com.cherry.common.core.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页
 *
 * @author keer
 * @date 2025-05-21
 */
@RequiredArgsConstructor
@RestController
public class IndexController {
  // todo

  @GetMapping("/")
  public String index() {
    return StringUtils.format("欢迎使用{}后台管理框架，请通过前端地址访问。", SpringUtils.getApplicationName());
  }
}
