package com.cherry.common.web.config.properties;

import com.cherry.common.web.enums.CaptchaCategory;
import com.cherry.common.web.enums.CaptchaType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 验证码 配置属性
 * @author keer
 * @date 2025-05-22
 */
@Data
@ConfigurationProperties(prefix = "captcha")
public class CaptchaProperties {

    private Boolean enable;

    /**
     * 验证码类型
     */
    private CaptchaType type;

    /**
     * 验证码类别
     */
    private CaptchaCategory category;

    /**
     * 数字验证码位数
     */
    private Integer numberLength;

    /**
     * 字符验证码长度
     */
    private Integer charLength;
}
