package com.cherry.web.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import com.cherry.common.core.utils.SpringUtils;
import com.cherry.common.core.utils.StringUtils;
import com.cherry.common.log.annotation.Log;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页
 *
 * @author keer
 * @date 2025-05-21
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class IndexController {
  // todo

    @SaCheckLogin
  @GetMapping("/")
  public String index() {
    return StringUtils.format("欢迎使用{}后台管理框架，请通过前端地址访问。", SpringUtils.getApplicationName());
  }
}
