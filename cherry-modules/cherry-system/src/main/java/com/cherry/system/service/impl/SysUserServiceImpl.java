package com.cherry.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.*;
import com.cherry.common.core.constant.CacheNames;
import com.cherry.common.core.constant.SystemConstants;
import com.cherry.common.core.exception.ServiceException;
import com.cherry.common.core.service.UserService;
import com.cherry.common.core.utils.*;
import com.cherry.common.flex.core.page.PageQuery;
import com.cherry.common.flex.core.page.TableDataInfo;
import com.cherry.common.flex.utils.SearchUtils;
import com.cherry.common.satoken.utils.LoginHelper;
import com.cherry.system.domain.*;
import com.cherry.system.domain.bo.SysUserBo;
import com.cherry.system.domain.vo.SysPostVo;
import com.cherry.system.domain.vo.SysRoleVo;
import com.cherry.system.domain.vo.SysUserVo;
import com.cherry.system.mapper.*;
import com.cherry.system.service.ISysDeptService;
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
import org.springframework.transaction.annotation.Transactional;

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
  private final SysUserRoleMapper userRoleMapper;
  private final SysUserPostMapper userPostMapper;
  private final SysDataScopeServiceImpl dataScopeService;
  private final ISysDeptService deptService;

  @Override
  public SysUserVo selectUserById(String userId) {
    QueryWrapper queryWrapper = QueryWrapper.create().eq(SysUser::getId, userId);
    log.info("selectUserById sql 1:{}", queryWrapper.toSQL());
    dataScopeService.getQueryWithDataScope(queryWrapper);
    log.info("selectUserById sql 2:{}", queryWrapper.toSQL());
    //    SearchUtils.getQueryDataScope(
    //        QueryWrapper.create().where("1 = 1"),
    //        DataColumn.of("deptName", "dept_id"),
    //        DataColumn.of("userName", "sys_user.id"));
    //      Optional<SysUser> userOpt = this.getByIdOpt(userId);
    Optional<SysUser> userOpt = this.getOneOpt(queryWrapper);
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
            QueryWrapper.create()
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
            QueryWrapper.create()
                .eq(SysUser::getEmail, user.getEmail())
                .ne(SysUser::getId, user.getId()));
    return !exist;
  }

  /**
   * 校验用户名称是否唯一
   *
   * @param user 用户信息
   * @return 结果
   */
  @Override
  public boolean checkUserNameUnique(SysUserBo user) {
    boolean exist =
        this.exists(
            QueryWrapper.create()
                .eq(SysUser::getUserName, user.getUserName())
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
        this.getOne(QueryWrapper.create().select(SysUser::getNickName).eq(SysUser::getId, userId));
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
        this.getOne(QueryWrapper.create().select(SysUser::getUserName).eq(SysUser::getId, userId));
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
    log.info("selectPageUserList sql 1:{}", qw.toSQL());
    dataScopeService.getQueryWithDataScope(qw);
    log.info("selectPageUserList sql 2:{}", qw.toSQL());
    //    DataColumn[] columns = {DataColumn.of("deptName", "sys_user.dept_id"),
    // DataColumn.of("userName", "sys_user.user_id")};
    //    getQueryPerm(
    //        qw, columns);
    //    SearchUtils.getQueryDataScope(
    //        QueryWrapper.create().where("1=1"),
    //        DataColumn.of("deptName", "dept_id"),
    //        DataColumn.of("userName", "id"));
    Page<SysUserVo> page = this.pageAs(pageQuery.build(), qw, SysUserVo.class);
    return TableDataInfo.build(page);
  }

  /**
   * 校验用户是否允许操作
   *
   * @param userId 用户ID
   */
  @Override
  public void checkUserAllowed(String userId) {
    if (ObjectUtil.isNotNull(userId) && LoginHelper.isSuperAdmin(userId)) {
      throw new ServiceException("不允许操作超级管理员用户");
    }
  }

  /**
   * 校验用户是否有数据权限
   *
   * @param userId 用户id
   */
  @Override
  public void checkUserDataScope(String userId) {
    if (ObjectUtil.isNull(userId) || LoginHelper.isSuperAdmin()) {
      return;
    }
    QueryWrapper queryWrapper = QueryWrapper.create().eq(SysUser::getId, userId);
    //    SearchUtils.getQueryDataScope(
    //        QueryWrapper.create().where("1=1"), DataColumn.of("deptName", "dept_id"),
    // DataColumn.of("userName", "id"));
    if (NumberUtil.equals(this.count(queryWrapper), 0)) {
      throw new ServiceException("没有权限访问用户数据！");
    }
    ;
  }

  private QueryWrapper buildQueryWrapper(SysUserBo user, PageQuery pageQuery) {
    Map<String, Object> params = user.getParams();

    QueryWrapper qw =
        QueryWrapper.create()
            .select()
            .from(SysUser.class)
            .eq(SysUser::getId, user.getId())
            .in(
                SysUser::getId,
                StringUtils.splitList(user.getId()),
                StrUtil.isNotBlank(user.getId()))
            .like(SysUser::getUserName, user.getUserName())
            .eq(SysUser::getStatus, user.getStatus())
            .like(SysUser::getPhonenumber, user.getPhonenumber())
            .notIn(
                SysUser::getId,
                StringUtils.splitList(user.getExcludeUserIds()),
                StrUtil.isNotBlank(user.getId()));

    if (ObjectUtil.isNotNull(user.getDeptId())) {
      // 部门树搜索
      qw.and(
          x -> {
            List<SysDept> deptList =deptService.selectListByParentId(user.getDeptId());
            List<String> deptIds = StreamUtils.toList(deptList, SysDept::getId);
            deptIds.add(user.getDeptId());
            x.in(SysUser::getDeptId, deptIds);
          });
    }

    SearchUtils.buildTimeBetween(qw, "create_time", params.get("beginTime"), params.get("endTime"));

    if (ObjUtil.isNotNull(pageQuery)) {
      pageQuery.buildOrders(qw);
    }

    return qw;
  }

  /**
   * 修改保存用户信息
   *
   * @param user 用户信息
   * @return 结果
   */
  @Override
  @CacheEvict(cacheNames = CacheNames.SYS_NICKNAME, key = "#user.userId")
  @Transactional(rollbackFor = Exception.class)
  public int updateUser(SysUserBo user) {
    // 新增用户与角色管理
    insertUserRole(user, true);
    // 新增用户与岗位管理
    insertUserPost(user, true);
    SysUser sysUser = MapstructUtils.convert(user, SysUser.class);
    sysUser.setPassword(null);
    // 防止错误更新后导致的数据误删除
    int flag = userMapper.update(sysUser);
    if (flag < 1) {
      throw new ServiceException("修改用户" + user.getUserName() + "信息失败");
    }
    return flag;
  }

  /**
   * 新增用户角色信息
   *
   * @param user 用户对象
   * @param clear 清除已存在的关联数据
   */
  private void insertUserRole(SysUserBo user, boolean clear) {
    this.insertUserRole(user.getId(), user.getRoleIds(), clear);
  }

  /**
   * 新增用户角色信息
   *
   * @param userId 用户ID
   * @param roleIds 角色组
   * @param clear 清除已存在的关联数据
   */
  private void insertUserRole(String userId, String[] roleIds, boolean clear) {

    if (ArrayUtil.isNotEmpty(roleIds)) {
      List<String> roleList = new ArrayList<>(List.of(roleIds));
      if (!LoginHelper.isSuperAdmin(userId)) {
        roleList.remove(SystemConstants.SUPER_ADMIN_ID);
      }
      // 判断是否具有此角色的操作权限
      //        todo 数据权限加上 2,3,4 d.dept_id ,  5：role.createor
      QueryWrapper roleQueryWrapper =
          QueryWrapper.create().from(SysRole.class).in(SysRole::getId, roleList);
      //       SearchUtils.getQueryDataScope(QueryWrapper.create().where("1=1"),
      // DataColumn.of("deptName", "sys_dept.dept_id"), DataColumn.of("userName", "creator"));
      List<SysRoleVo> roles = roleMapper.selectListByQueryAs(roleQueryWrapper, SysRoleVo.class);
      //          roleMapper.selectRoleList(new QueryWrapper<SysRole>().in("r.role_id", roleList));
      if (CollUtil.isEmpty(roles)) {
        throw new ServiceException("没有权限访问角色的数据");
      }
      if (clear) {
        // 删除用户与角色关联
        userRoleMapper.deleteByQuery(QueryWrapper.create().eq(SysUserRole::getUserId, userId));
      }
      // 新增用户与角色管理
      List<SysUserRole> list =
          StreamUtils.toList(
              roleList,
              roleId -> {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(userId);
                ur.setRoleId(roleId);
                return ur;
              });
      userRoleMapper.insertBatch(list);
    }
  }

  /**
   * 新增用户岗位信息
   *
   * @param user 用户对象
   * @param clear 清除已存在的关联数据
   */
  private void insertUserPost(SysUserBo user, boolean clear) {
    String[] posts = user.getPostIds();
    if (ArrayUtil.isNotEmpty(posts)) {
      if (clear) {
        // 删除用户与岗位关联
        userPostMapper.deleteByQuery(
            QueryWrapper.create().in(SysUserPost::getUserId, user.getId()));
      }
      // 新增用户与岗位管理
      List<SysUserPost> list =
          StreamUtils.toList(
              List.of(posts),
              postId -> {
                SysUserPost up = new SysUserPost();
                up.setUserId(user.getId());
                up.setPostId(postId);
                return up;
              });
      userPostMapper.insertBatch(list);
    }
  }
}
