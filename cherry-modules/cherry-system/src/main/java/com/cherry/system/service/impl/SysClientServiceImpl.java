package com.cherry.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cherry.system.domain.SysClient;
import com.cherry.system.domain.vo.SysClientVo;
import com.cherry.system.mapper.SysClientMapper;
import com.cherry.system.service.ISysClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author keer
 * @date 2025-05-23
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SysClientServiceImpl implements ISysClientService {
  // todo

  private final SysClientMapper baseMapper;

  /** 查询客户端管理 */
  // todo
  @Override
  public SysClientVo queryByClientId(String clientId) {
    return baseMapper.selectVoOne(
        new LambdaQueryWrapper<SysClient>().eq(SysClient::getClientId, clientId));
  }
}
