package com.cherry.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.cherry.common.core.utils.StringUtils;
import com.cherry.system.domain.vo.SysRoleVo;
import com.cherry.system.mapper.SysRoleMapper;
import com.cherry.system.service.ISysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 角色 业务层处理
 *
 * @author keer
 * @date 2025-05-27
 */
@RequiredArgsConstructor
@Service
public class SysRoleServiceImpl implements ISysRoleService {
  // todo

  private final SysRoleMapper baseMapper;

  /**
   * 根据用户ID查询权限
   *
   * @param userId 用户ID
   * @return 权限列表
   */
  @Override
  public Set<String> selectRolePermissionByUserId(Long userId) {
    List<SysRoleVo> perms = baseMapper.selectRolesByUserId(userId);
    Set<String> permsSet = new HashSet<>();
    for (SysRoleVo perm : perms) {
      if (ObjectUtil.isNotNull(perm)) {
        permsSet.addAll(StringUtils.splitList(perm.getRoleKey().trim()));
      }
    }
    return permsSet;
  }

    /**
     * 根据用户ID查询角色
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    @Override
    public List<SysRoleVo> selectRolesByUserId(Long userId) {
        return baseMapper.selectRolesByUserId(userId);
    }
}
