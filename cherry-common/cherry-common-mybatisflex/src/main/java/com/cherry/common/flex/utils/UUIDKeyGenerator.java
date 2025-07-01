package com.cherry.common.flex.utils;

import cn.hutool.core.util.IdUtil;
import com.mybatisflex.core.keygen.IKeyGenerator;

/**
 * UUIDKeyGenerator
 *
 * @author keer2345
 * @date 2025-06-24
 */
public class UUIDKeyGenerator implements IKeyGenerator {
  @Override
  public Object generate(Object o, String s) {
    //        return UUID.randomUUID().toString().replace("-", "");
    return IdUtil.simpleUUID();
  }
}
