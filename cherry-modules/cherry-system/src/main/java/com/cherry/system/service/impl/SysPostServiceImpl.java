package com.cherry.system.service.impl;

import com.cherry.common.core.utils.MapstructUtils;
import com.cherry.system.domain.SysPost;
import com.cherry.system.domain.vo.SysPostVo;
import com.cherry.system.mapper.SysPostMapper;
import com.cherry.system.service.ISysPostService;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 岗位信息 服务层处理
 *
 * @author keer
 * @date 2025-05-27
 */
@RequiredArgsConstructor
@Service
public class SysPostServiceImpl extends ServiceImpl<SysPostMapper, SysPost>
    implements ISysPostService {
  // todo
  private final SysPostMapper postMapper;

  @Override
  public List<SysPostVo> selectPostsByUserId(String userId) {
    return MapstructUtils.convert(postMapper.selectPostsByUserId(userId), SysPostVo.class);
  }
}
