package com.cherry.system.service.impl;

import com.cherry.system.domain.SysTenant;
import com.cherry.system.domain.bo.SysTenantBo;
import com.cherry.system.domain.vo.SysTenantVo;
import com.cherry.system.mapper.SysTenantMapper;
import com.cherry.system.service.ISysTenantService;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author keer
 * @date 2025-05-22
 */
@RequiredArgsConstructor
@Service
public class SysTenantServiceImpl extends ServiceImpl<SysTenantMapper, SysTenant>
    implements ISysTenantService {
  // todo

  private final SysTenantMapper tenantMapper;

  /** 查询租户列表 */
  @Override
  public List<SysTenantVo> queryList(SysTenantBo bo) {
    return this.listAs(buildQueryWrapper(bo), SysTenantVo.class);
    //    return MapstructUtils.convert(
    //        tenantMapper.selectListByQuery(buildQueryWrapper(bo)), SysTenantVo.class);
  }

  /** 基于租户ID查询租户 */
  @Override
  public SysTenantVo queryByTenantId(String tenantId) {
    return this.getOneAs(
        new QueryWrapper().eq(SysTenant::getTenantId, tenantId), SysTenantVo.class);
  }

  private QueryWrapper buildQueryWrapper(SysTenantBo bo) {
    QueryWrapper queryWrapper = new QueryWrapper();
    queryWrapper
        .create()
        .select()
        .from(SysTenant.class)
        .eq(SysTenant::getTenantId, bo.getTenantId())
        .like(SysTenant::getContactUserName, bo.getContactUserName())
        .eq(SysTenant::getContactPhone, bo.getContactPhone())
        .like(SysTenant::getCompanyName, bo.getCompanyName())
        .eq(SysTenant::getLicenseNumber, bo.getLicenseNumber())
        .like(SysTenant::getAddress, bo.getAddress())
        .like(SysTenant::getIntro, bo.getIntro())
        .like(SysTenant::getDomain, bo.getDomain())
        .eq(SysTenant::getPackageId, bo.getPackageId())
        .eq(SysTenant::getExpireTime, bo.getExpireTime())
        .eq(SysTenant::getAccountCount, bo.getAccountCount())
        .eq(SysTenant::getStatus, bo.getStatus())
        .orderBy(SysTenant::getCreateTime);

    return queryWrapper;
  }
}
