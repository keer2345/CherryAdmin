package com.cherry.system.mapper;

import com.cherry.common.mybatis.core.mapper.BaseMapperPlus;
import com.cherry.system.domain.SysMenu;
import com.cherry.system.domain.vo.SysMenuVo;

import java.util.List;

/**
 * 菜单表 数据层
 *
 * @author keer
 * @date 2025-05-26
 */
public interface SysMenuMapper extends BaseMapperPlus<SysMenu, SysMenuVo> {
  // todo

  /**
   * 根据用户ID查询权限
   *
   * @param userId 用户ID
   * @return 权限列表
   */
  List<String> selectMenuPermsByUserId(Long userId);
}
