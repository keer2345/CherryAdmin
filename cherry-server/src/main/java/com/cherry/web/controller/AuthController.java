package com.cherry.web.controller;

import com.cherry.common.core.domain.R;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author keer
 * @date 2025-05-21
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
  // todo

  /**
   * 登录方法
   *
   * @param body
   */
  @PostMapping("/login")
  public R<String> login(@RequestBody String body) {

     return R.ok("");
  }
}
