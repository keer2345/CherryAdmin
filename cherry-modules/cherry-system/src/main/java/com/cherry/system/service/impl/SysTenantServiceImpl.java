package com.cherry.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cherry.common.core.utils.StringUtils;
import com.cherry.system.domain.SysTenant;
import com.cherry.system.domain.bo.SysTenantBo;
import com.cherry.system.domain.vo.SysTenantVo;
import com.cherry.system.mapper.SysTenantMapper;
import com.cherry.system.service.ISysTenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author keer
 * @date 2025-05-22
 */
@RequiredArgsConstructor
@Service
public class SysTenantServiceImpl implements ISysTenantService {
  // todo

  private final SysTenantMapper baseMapper;

  /** 查询租户列表 */
  @Override
  public List<SysTenantVo> queryList(SysTenantBo bo) {
    LambdaQueryWrapper<SysTenant> lqw = buildQueryWrapper(bo);
    return baseMapper.selectVoList(lqw);
  }

  /** 基于租户ID查询租户 */
  @Override
  public SysTenantVo queryByTenantId(String tenantId) {
    return baseMapper.selectVoOne(
        new LambdaQueryWrapper<SysTenant>().eq(SysTenant::getTenantId, tenantId));
  }

  private LambdaQueryWrapper<SysTenant> buildQueryWrapper(SysTenantBo bo) {
    LambdaQueryWrapper<SysTenant> lqw = Wrappers.lambdaQuery();
    lqw.eq(StringUtils.isNotBlank(bo.getTenantId()), SysTenant::getTenantId, bo.getTenantId());
    lqw.like(
        StringUtils.isNotBlank(bo.getContactUserName()),
        SysTenant::getContactUserName,
        bo.getContactUserName());
    lqw.eq(
        StringUtils.isNotBlank(bo.getContactPhone()),
        SysTenant::getContactPhone,
        bo.getContactPhone());
    lqw.like(
        StringUtils.isNotBlank(bo.getCompanyName()),
        SysTenant::getCompanyName,
        bo.getCompanyName());
    lqw.eq(
        StringUtils.isNotBlank(bo.getLicenseNumber()),
        SysTenant::getLicenseNumber,
        bo.getLicenseNumber());
    lqw.eq(StringUtils.isNotBlank(bo.getAddress()), SysTenant::getAddress, bo.getAddress());
    lqw.eq(StringUtils.isNotBlank(bo.getIntro()), SysTenant::getIntro, bo.getIntro());
    lqw.like(StringUtils.isNotBlank(bo.getDomain()), SysTenant::getDomain, bo.getDomain());
    lqw.eq(bo.getPackageId() != null, SysTenant::getPackageId, bo.getPackageId());
    lqw.eq(bo.getExpireTime() != null, SysTenant::getExpireTime, bo.getExpireTime());
    lqw.eq(bo.getAccountCount() != null, SysTenant::getAccountCount, bo.getAccountCount());
    lqw.eq(StringUtils.isNotBlank(bo.getStatus()), SysTenant::getStatus, bo.getStatus());
    lqw.orderByAsc(SysTenant::getId);
    return lqw;
  }
}
