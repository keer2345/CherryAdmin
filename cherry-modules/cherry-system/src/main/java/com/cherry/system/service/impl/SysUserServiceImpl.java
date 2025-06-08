package com.cherry.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cherry.common.core.constant.CacheNames;
import com.cherry.common.core.service.UserService;
import com.cherry.common.core.utils.ObjectUtils;
import com.cherry.common.core.utils.SpringUtils;
import com.cherry.common.core.utils.StreamUtils;
import com.cherry.common.core.utils.StringUtils;
import com.cherry.system.domain.SysUser;
import com.cherry.system.domain.vo.SysPostVo;
import com.cherry.system.domain.vo.SysRoleVo;
import com.cherry.system.domain.vo.SysUserVo;
import com.cherry.system.mapper.SysPostMapper;
import com.cherry.system.mapper.SysRoleMapper;
import com.cherry.system.mapper.SysUserMapper;
import com.cherry.system.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
public class SysUserServiceImpl implements ISysUserService, UserService {
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

  /**
   * 通过用户ID查询用户账户
   *
   * @param userIds 用户ID 多个用逗号隔开
   * @return 用户账户
   */
  @Override
  public String selectNicknameByIds(String userIds) {
    List<String> list = new ArrayList<>();
    for (Long id : StringUtils.splitTo(userIds, Convert::toLong)) {
      String nickname = SpringUtils.getAopProxy(this).selectNicknameById(id);
      if (StringUtils.isNotBlank(nickname)) {
        list.add(nickname);
      }
    }
    return String.join(StringUtils.SEPARATOR, list);
  }

  /**
   * 通过用户ID查询用户账户
   *
   * @param userId 用户ID
   * @return 用户账户
   */
  @Override
  @Cacheable(cacheNames = CacheNames.SYS_NICKNAME, key = "#userId")
  public String selectNicknameById(Long userId) {
    SysUser sysUser =
        baseMapper.selectOne(
            new LambdaQueryWrapper<SysUser>()
                .select(SysUser::getNickName)
                .eq(SysUser::getUserId, userId));
    return ObjectUtils.notNullGetter(sysUser, SysUser::getNickName);
  }

  /**
   * 通过用户ID查询用户账户
   *
   * @param userId 用户ID
   * @return 用户账户
   */
  @Cacheable(cacheNames = CacheNames.SYS_USER_NAME, key = "#userId")
  @Override
  public String selectUserNameById(Long userId) {
    SysUser sysUser =
        baseMapper.selectOne(
            new LambdaQueryWrapper<SysUser>()
                .select(SysUser::getUserName)
                .eq(SysUser::getUserId, userId));
    return ObjectUtils.notNullGetter(sysUser, SysUser::getUserName);
  }
}
