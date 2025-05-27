package com.cherry.system.service.impl;

import com.cherry.common.core.utils.StringUtils;
import com.cherry.system.mapper.SysMenuMapper;
import com.cherry.system.service.ISysMenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 菜单 业务层处理
 *
 * @author keer
 * @date 2025-05-26
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SysMenuServiceImpl implements ISysMenuService {
  // todo

  private final SysMenuMapper baseMapper;

  /**
   * 根据用户ID查询权限
   *
   * @param userId 用户ID
   * @return 权限列表
   */
  @Override
  public Set<String> selectMenuPermsByUserId(Long userId) {
    List<String> perms = baseMapper.selectMenuPermsByUserId(userId);
    Set<String> permsSet = new HashSet<>();
    for (String perm : perms) {
      if (StringUtils.isNotEmpty(perm)) {
        permsSet.addAll(StringUtils.splitList(perm.trim()));
      }
    }
    return permsSet;
  }
}
