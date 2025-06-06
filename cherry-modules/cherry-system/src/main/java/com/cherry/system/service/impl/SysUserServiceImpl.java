package com.cherry.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.cherry.common.core.utils.StreamUtils;
import com.cherry.common.core.utils.StringUtils;
import com.cherry.system.domain.vo.SysPostVo;
import com.cherry.system.domain.vo.SysRoleVo;
import com.cherry.system.domain.vo.SysUserVo;
import com.cherry.system.mapper.SysPostMapper;
import com.cherry.system.mapper.SysRoleMapper;
import com.cherry.system.mapper.SysUserMapper;
import com.cherry.system.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户 业务层处理
 *
 * @author keer
 * @date 2025-05-28
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SysUserServiceImpl implements ISysUserService {
  // todo
  private final SysUserMapper baseMapper;
  private final SysRoleMapper roleMapper;
  private final SysPostMapper postMapper;

  @Override
  public SysUserVo selectUserById(Long userId) {
    SysUserVo user = baseMapper.selectVoById(userId);
    if (ObjUtil.isNull(user)) {
      return user;
    }
    user.setRoles(roleMapper.selectRolesByUserId(user.getUserId()));
    return user;
  }

  /**
   * 查询用户所属角色组
   *
   * @param userId 用户ID
   * @return 结果
   */
  @Override
  public String selectUserRoleGroup(Long userId) {
    List<SysRoleVo> list = roleMapper.selectRolesByUserId(userId);
    if (CollUtil.isEmpty(list)) {
      return StringUtils.EMPTY;
    }
    return StreamUtils.join(list, SysRoleVo::getRoleName);
  }

  /**
   * 查询用户所属岗位组
   *
   * @param userId 用户ID
   * @return 结果
   */
  @Override
  public String selectUserPostGroup(Long userId) {

    List<SysPostVo> list = postMapper.selectPostsByUserId(userId);
    if (CollUtil.isEmpty(list)) {
      return StringUtils.EMPTY;
    }
    return StreamUtils.join(list, SysPostVo::getPostName);
  }
}
