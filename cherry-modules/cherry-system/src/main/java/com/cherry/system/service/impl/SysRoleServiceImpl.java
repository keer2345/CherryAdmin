package com.cherry.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.cherry.common.core.utils.CollectionUtils;
import com.cherry.common.core.utils.MapstructUtils;
import com.cherry.common.core.utils.StringUtils;
import com.cherry.system.domain.SysRole;
import com.cherry.system.domain.vo.SysRoleVo;
import com.cherry.system.mapper.SysRoleMapper;
import com.cherry.system.service.ISysRoleService;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Provider;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.cherry.common.core.utils.CollectionUtils.convertSet;

/**
 * 角色 业务层处理
 *
 * @author keer
 * @date 2025-05-27
 */
@RequiredArgsConstructor
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole>
    implements ISysRoleService {
  // todo

  private final SysRoleMapper roleMapper;

  /**
   * 根据用户ID查询权限
   *
   * @param userId 用户ID
   * @return 权限列表
   */
  @Override
  public Set<String> selectRolePermissionByUserId(String userId) {
    List<SysRole> perms = roleMapper.selectRolesByUserId(userId);
    return convertSet(perms, SysRole::getRoleKey);
  }

  /**
   * 根据用户ID查询角色
   *
   * @param userId 用户ID
   * @return 角色列表
   */
  @Override
  public List<SysRoleVo> selectRolesByUserId(String userId) {
    return MapstructUtils.convert(roleMapper.selectRolesByUserId(userId), SysRoleVo.class);
  }
}
