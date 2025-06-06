package com.cherry.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cherry.system.domain.bo.SysSocial;
import com.cherry.system.domain.vo.SysSocialVo;
import com.cherry.system.mapper.SysSocialMapper;
import com.cherry.system.service.ISysSocialService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 社会化关系Service业务层处理
 *
 * @author keer
 * @date 2025-06-06
 */
@RequiredArgsConstructor
@Service
public class SysSocialServiceImpl implements ISysSocialService {
  // todo
  private final SysSocialMapper baseMapper;

  @Override
  public List<SysSocialVo> queryListByUserId(Long userId) {
    return baseMapper.selectVoList(
        new LambdaQueryWrapper<SysSocial>().eq(SysSocial::getUserId, userId));
  }
}
