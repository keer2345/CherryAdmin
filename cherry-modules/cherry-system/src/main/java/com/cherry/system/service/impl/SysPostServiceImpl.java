package com.cherry.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.cherry.common.core.constant.SystemConstants;
import com.cherry.common.core.utils.MapstructUtils;
import com.cherry.common.core.utils.StreamUtils;
import com.cherry.common.flex.core.page.PageQuery;
import com.cherry.system.domain.SysDept;
import com.cherry.system.domain.SysPost;
import com.cherry.system.domain.bo.SysPostBo;
import com.cherry.system.domain.vo.SysPostVo;
import com.cherry.system.mapper.SysDeptMapper;
import com.cherry.system.mapper.SysPostMapper;
import com.cherry.system.service.ISysDeptService;
import com.cherry.system.service.ISysPostService;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class SysPostServiceImpl extends ServiceImpl<SysPostMapper, SysPost>
    implements ISysPostService {
  // todo
  private final SysPostMapper postMapper;
  //  private final SysDeptMapper deptMapper;
  private final ISysDeptService deptService;

  @Override
  public List<SysPostVo> selectPostsByUserId(String userId) {
    return MapstructUtils.convert(postMapper.selectPostsByUserId(userId), SysPostVo.class);
  }

  @Override
  public List<SysPostVo> selectPostList(SysPostBo post) {
    return this.listAs(buildQueryWrapper(post), SysPostVo.class);
  }

  @Override
  public List<SysPostVo> selectPostByIds(List<String> postIds) {
    return this.listAs(
        new QueryWrapper()
            .select(SysPost::getId, SysPost::getPostName, SysPost::getPostCode)
            .eq(SysPost::getStatus, SystemConstants.NORMAL)
            .in(SysPost::getId, postIds, CollUtil.isNotEmpty(postIds)),
        SysPostVo.class);
  }

  /**
   * 根据查询条件构建查询包装器
   *
   * @param bo 查询条件对象
   * @return 构建好的查询包装器
   */
  private QueryWrapper buildQueryWrapper(SysPostBo bo) {
    QueryWrapper qw = new QueryWrapper();

    qw.like(SysPost::getPostCode, bo.getPostCode())
        .like(SysPost::getPostCategory, bo.getPostCategory())
        .like(SysPost::getPostName, bo.getPostName())
        .eq(SysPost::getStatus, bo.getStatus());

    //            .orderByAsc(SysPost::getPostSort);
    if (ObjectUtil.isNotNull(bo.getDeptId())) {
      // 优先单部门搜索
      qw.eq(SysPost::getDeptId, bo.getDeptId());
    } else if (ObjectUtil.isNotNull(bo.getBelongDeptId())) {
      // 部门树搜索
      qw.and(
          x -> {
            List<SysDept> deptList = deptService.selectListByParentId(bo.getBelongDeptId());
            List<String> deptIds = StreamUtils.toList(deptList, SysDept::getId);
            deptIds.add(bo.getBelongDeptId());
            x.in(SysPost::getDeptId, deptIds);
          });
    }
    qw.orderBy(SysPost::getPostSort, true);
    return qw;
  }
}
