package com.cherry.system.service;

import com.cherry.system.domain.vo.SysSocialVo;

import java.util.List;

/**
 * 社会化关系Service接口
 *
 * @author keer
 * @date 2025-06-06
 */
public interface ISysSocialService {
  // todo

  /** 查询社会化关系列表 */
  List<SysSocialVo> queryListByUserId(Long userId);
}
