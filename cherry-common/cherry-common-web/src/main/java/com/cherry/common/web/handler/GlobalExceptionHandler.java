package com.cherry.common.web.handler;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpStatus;
import com.cherry.common.core.domain.R;
import com.cherry.common.core.exception.ServiceException;
import com.cherry.common.core.exception.base.BaseException;
import com.cherry.common.core.utils.StreamUtils;
import com.cherry.common.json.utils.JsonUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.IOException;

/**
 * 全局异常处理器
 *
 * @author keer
 * @date 2025-05-23
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
  // todo

  /** 请求方式不支持 */

  /** 业务异常 */

  /** 认证失败 */

  /** servlet异常 */

  /** 业务异常 */
  @ExceptionHandler(BaseException.class)
  public R<Void> handleBaseException(BaseException e, HttpServletRequest request) {
    log.error(e.getMessage());
    return R.fail(e.getMessage());
  }

  /** 请求路径中缺少必需的路径变量 */

  /** 请求参数类型不匹配 */

  /** 找不到路由 */

  /** 拦截未知的运行时异常 */

  /** 拦截未知的运行时异常 */

  /** 系统异常 */

  /** 自定义验证异常 */

  /** 自定义验证异常 */

  /** 自定义验证异常 */
}
