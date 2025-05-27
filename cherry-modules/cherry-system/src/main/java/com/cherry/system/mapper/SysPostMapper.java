package com.cherry.system.mapper;

import com.cherry.common.mybatis.core.mapper.BaseMapperPlus;
import com.cherry.system.domain.SysPost;
import com.cherry.system.domain.vo.SysPostVo;

import java.util.List;

/**
 * 岗位信息 数据层
 *
 * @author keer
 * @date 2025-05-27
 */
public interface SysPostMapper extends BaseMapperPlus<SysPost, SysPostVo> {
  // todo
  /**
   * 查询用户所属岗位组
   *
   * @param userId 用户ID
   * @return 结果
   */
  List<SysPostVo> selectPostsByUserId(Long userId);
}
