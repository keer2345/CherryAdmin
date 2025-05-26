package com.cherry.system.service.impl;

import com.cherry.system.mapper.SysMenuMapper;
import com.cherry.system.service.ISysMenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
      List<String> perms=baseMapper.selectMenuPermsByUserId(userId);
      log.info("perms: {}",perms);
    return Set.of();
  }
}
