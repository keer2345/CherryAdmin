package com.cherry.system.service.impl;

import com.cherry.common.core.utils.MapstructUtils;
import com.cherry.system.domain.SysSocial;
import com.cherry.system.domain.vo.SysSocialVo;
import com.cherry.system.mapper.SysSocialMapper;
import com.cherry.system.service.ISysSocialService;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
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
public class SysSocialServiceImpl extends ServiceImpl<SysSocialMapper, SysSocial>
    implements ISysSocialService {
  // todo
  private final SysSocialMapper socialMapper;

  @Override
  public List<SysSocialVo> queryListByUserId(String userId) {
    //    return socialMapper.selectVoList(
    //        new LambdaQueryWrapper<SysSocial>().eq(SysSocial::getUserId, userId));
//    return MapstructUtils.convert(socialMapper.selectListByUserId(userId), SysSocialVo.class);
    return this.listAs(new QueryWrapper().eq(SysSocial::getUserId,userId), SysSocialVo.class);
  }
}
