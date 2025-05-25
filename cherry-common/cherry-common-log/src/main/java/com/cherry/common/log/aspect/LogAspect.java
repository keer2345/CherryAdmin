package com.cherry.common.log.aspect;

import com.cherry.common.log.annotation.Log;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.boot.autoconfigure.AutoConfiguration;

/**
 * 操作日志记录处理
 *
 * @author keer
 * @date 2025-05-23
 */
@Slf4j
@Aspect
@AutoConfiguration
public class LogAspect {
  // todo

  /** 排除敏感属性字段 */
  public static final String[] EXCLUDE_PROPERTIES = {
    "password", "oldPassword", "newPassword", "confirmPassword"
  };

  /** 计时 key */
  private static final ThreadLocal<StopWatch> KEY_CACHE = new ThreadLocal<>();

  /** 处理请求前执行 */
  @Before(value = "@annotation(controllerLog)")
  public void doBefore(JoinPoint joinPoint, Log controllerLog) {
    log.info("log before ...");
  }

  /**
   * 处理完请求后执行
   *
   * @param joinPoint 切点
   */
  @AfterReturning(pointcut = "@annotation(controllerLog)", returning = "jsonResult")
  public void doAfterReturning(JoinPoint joinPoint, Log controllerLog, Object jsonResult) {
    // handleLog(joinPoint, controllerLog, null, jsonResult);
      log.info("log after ...");
  }
}
