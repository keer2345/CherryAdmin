package com.cherry.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.cherry.common.core.constant.CacheNames;
import com.cherry.common.core.service.UserService;
import com.cherry.common.core.utils.*;
import com.cherry.common.flex.permission.DataColumn;
import com.cherry.common.flex.permission.DataPermission;
import com.cherry.common.flex.utils.SearchUtils;
import com.cherry.common.flex.core.page.PageQuery;
import com.cherry.common.flex.core.page.TableDataInfo;
import com.cherry.system.domain.SysUser;
import com.cherry.system.domain.bo.SysUserBo;
import com.cherry.system.domain.vo.SysPostVo;
import com.cherry.system.domain.vo.SysRoleVo;
import com.cherry.system.domain.vo.SysUserVo;
import com.cherry.system.mapper.SysPostMapper;
import com.cherry.system.mapper.SysRoleMapper;
import com.cherry.system.mapper.SysUserMapper;
import com.cherry.system.service.ISysUserService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.util.UpdateEntity;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 用户 业务层处理
 *
 * @author keer
 * @date 2025-05-28
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
    implements ISysUserService, UserService {
  // todo
  private final SysUserMapper userMapper;
  private final SysRoleMapper roleMapper;
  private final SysPostMapper postMapper;

  @Override
  public SysUserVo selectUserById(String userId) {
    Optional<SysUser> userOpt = this.getByIdOpt(userId);
    if (userOpt.isEmpty()) {
      return null;
    }
    SysUserVo user = MapstructUtils.convert(userOpt.get(), SysUserVo.class);
    user.setRoles(
        MapstructUtils.convert(roleMapper.selectRolesByUserId(user.getId()), SysRoleVo.class));
    return user;
  }

  /**
   * 查询用户所属角色组
   *
   * @param userId 用户ID
   * @return 结果
   */
  @Override
  public String selectUserRoleGroup(String userId) {
    List<SysRoleVo> list =
        MapstructUtils.convert(roleMapper.selectRolesByUserId(userId), SysRoleVo.class);
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
  public String selectUserPostGroup(String userId) {

    List<SysPostVo> list =
        MapstructUtils.convert(postMapper.selectPostsByUserId(userId), SysPostVo.class);
    if (CollUtil.isEmpty(list)) {
      return StringUtils.EMPTY;
    }
    return StreamUtils.join(list, SysPostVo::getPostName);
  }

  /**
   * 校验手机号码是否唯一
   *
   * @param user 用户信息
   */
  @Override
  public boolean checkPhoneUnique(SysUserBo user) {
    boolean exist =
        this.exists(
            new QueryWrapper()
                .eq(SysUser::getPhonenumber, user.getPhonenumber())
                .ne(SysUser::getId, user.getId()));
    return !exist;
  }

  /**
   * 校验email是否唯一
   *
   * @param user 用户信息
   */
  @Override
  public boolean checkEmailUnique(SysUserBo user) {
    boolean exist =
        this.exists(
            new QueryWrapper()
                .eq(SysUser::getEmail, user.getEmail())
                .ne(SysUser::getId, user.getId()));
    return !exist;
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
    for (String id : StringUtils.splitList(userIds)) // splitTo(userIds, Convert::toLong))
    {
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
  public String selectNicknameById(String userId) {
    SysUser sysUser =
        this.getOne(new QueryWrapper().select(SysUser::getNickName).eq(SysUser::getId, userId));
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
  public String selectUserNameById(String userId) {
    SysUser sysUser =
        this.getOne(new QueryWrapper().select(SysUser::getUserName).eq(SysUser::getId, userId));
    return ObjectUtils.notNullGetter(sysUser, SysUser::getUserName);
  }

  /**
   * 修改用户基本信息
   *
   * @param userBo 用户信息
   * @return 结果
   */
  @CacheEvict(cacheNames = CacheNames.SYS_NICKNAME, key = "#user.userId")
  @Override
  public int updateUserProfile(SysUserBo userBo) {
    SysUser user = UpdateEntity.of(SysUser.class, userBo.getId());
    user.setNickName(userBo.getNickName());
    user.setPhonenumber(userBo.getPhonenumber());
    user.setEmail(userBo.getEmail());
    user.setSex(userBo.getSex());
    return userMapper.update(user);
  }

  /**
   * 重置用户密码
   *
   * @param userId 用户ID
   * @param password 密码
   * @return 结果
   */
  @Override
  public int resetUserPwd(String userId, String password) {

    SysUser user = UpdateEntity.of(SysUser.class, userId);
    user.setPassword(password);
    return userMapper.update(user);
  }

  @Override
  public TableDataInfo<SysUserVo> selectPageUserList(SysUserBo user, PageQuery pageQuery) {
    QueryWrapper qw = buildQueryWrapper(user, pageQuery);
//    DataColumn[] columns = {DataColumn.of("deptName", "sys_user.dept_id"), DataColumn.of("userName", "sys_user.user_id")};
//    getQueryPerm(
//        qw, columns);
      SearchUtils.getQueryDataScope(qw);
    log.info("query user sql: {}", qw.toSQL());
    Page<SysUserVo> page = this.pageAs(pageQuery.build(), qw, SysUserVo.class);
    return TableDataInfo.build(page);
  }

  private void getQueryPerm(QueryWrapper queryWrapper, DataColumn... columns) {
    DataPermission dataPermission = DataPermission.of(columns);
    dataPermission.handler(queryWrapper);
  }

  private QueryWrapper buildQueryWrapper(SysUserBo user, PageQuery pageQuery) {
    Map<String, Object> params = user.getParams();

    QueryWrapper qw = new QueryWrapper();
    qw.select()
        .from(SysUser.class)
        .eq(SysUser::getId, user.getId())
        .in(SysUser::getId, StringUtils.splitList(user.getId()), StrUtil.isNotBlank(user.getId()))
        .like(SysUser::getUserName, user.getUserName())
        .eq(SysUser::getStatus, user.getStatus())
        .like(SysUser::getPhonenumber, user.getPhonenumber())
        .notIn(
            SysUser::getId,
            StringUtils.splitList(user.getExcludeUserIds()),
            StrUtil.isNotBlank(user.getId()));

    SearchUtils.buildTimeBetween(qw, "create_time", params.get("beginTime"), params.get("endTime"));

    if (ObjUtil.isNotNull(pageQuery)) {
      pageQuery.buildOrders(qw);
    }

    return qw;
  }
}
