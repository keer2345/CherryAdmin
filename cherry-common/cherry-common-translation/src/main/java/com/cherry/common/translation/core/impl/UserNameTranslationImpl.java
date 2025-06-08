package com.cherry.common.translation.core.impl;

import com.cherry.common.core.service.UserService;
import com.cherry.common.translation.annotation.TranslationType;
import com.cherry.common.translation.constant.TransConstant;
import com.cherry.common.translation.core.TranslationInterface;
import lombok.AllArgsConstructor;

/**
 * 用户名翻译实现
 *
 * @author Lion Li
 */
@AllArgsConstructor
@TranslationType(type = TransConstant.USER_ID_TO_NAME)
public class UserNameTranslationImpl implements TranslationInterface<String> {

    private final UserService userService;

    @Override
    public String translation(Object key, String other) {
        if (key instanceof Long id) {
            return userService.selectUserNameById(id);
        }
        return null;
    }
}
