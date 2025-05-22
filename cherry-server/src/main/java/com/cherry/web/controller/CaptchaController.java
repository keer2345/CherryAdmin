package com.cherry.web.controller;

import cn.hutool.captcha.AbstractCaptcha;
import cn.hutool.captcha.generator.CodeGenerator;
import cn.hutool.core.util.IdUtil;
import com.cherry.common.core.constant.Constants;
import com.cherry.common.core.constant.GlobalConstants;
import com.cherry.common.core.domain.R;
import com.cherry.common.core.utils.SpringUtils;
import com.cherry.common.core.utils.StringUtils;
import com.cherry.common.core.utils.reflect.ReflectUtils;
import com.cherry.common.redis.utils.RedisUtils;
import com.cherry.common.web.config.properties.CaptchaProperties;
import com.cherry.common.web.enums.CaptchaType;
import com.cherry.web.domain.vo.CaptchaVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

/**
 * @author keer
 * @date 2025-05-22
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
public class CaptchaController {
  // todo

  private final CaptchaProperties captchaProperties;

  /** 生成验证码 */
  @GetMapping("/auth/code")
  public R<CaptchaVo> getCode() {
    boolean captchaEnabled = captchaProperties.getEnable();
    if (!captchaEnabled){
        CaptchaVo captchaVo=new CaptchaVo();
        captchaVo.setCaptchaEnabled(false);
        return R.ok(captchaVo);
    }
      return R.ok(SpringUtils.getAopProxy(this).getCodeImpl());
  }

    /**
     * 生成验证码
     * 独立方法避免验证码关闭之后仍然走限流
     */
    // todo
    // @RateLimiter(time = 60, count = 10, limitType = LimitType.IP)
    public CaptchaVo getCodeImpl() {
        // 保存验证码信息
        String uuid = IdUtil.simpleUUID();
        String verifyKey = GlobalConstants.CAPTCHA_CODE_KEY + uuid;
        // 生成验证码
        CaptchaType captchaType = captchaProperties.getType();
        boolean isMath = CaptchaType.MATH == captchaType;
        Integer length = isMath ? captchaProperties.getNumberLength() : captchaProperties.getCharLength();
        CodeGenerator codeGenerator = ReflectUtils.newInstance(captchaType.getClazz(), length);
        AbstractCaptcha captcha = SpringUtils.getBean(captchaProperties.getCategory().getClazz());
        captcha.setGenerator(codeGenerator);
        captcha.createCode();
        // 如果是数学验证码，使用SpEL表达式处理验证码结果
        String code = captcha.getCode();
        if (isMath) {
            ExpressionParser parser = new SpelExpressionParser();
            Expression exp = parser.parseExpression(StringUtils.remove(code, "="));
            code = exp.getValue(String.class);
        }

        RedisUtils.setCacheObject(verifyKey, code, Duration.ofMinutes(Constants.CAPTCHA_EXPIRATION));
        CaptchaVo captchaVo = new CaptchaVo();
        captchaVo.setUuid(uuid);
        captchaVo.setImg(captcha.getImageBase64());

        return captchaVo;
    }
}
