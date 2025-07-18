package com.cherry.system.service.impl;

import static com.cherry.common.core.utils.CollectionUtils.convertSet;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.cherry.common.core.utils.MapstructUtils;
import com.cherry.common.core.utils.StreamUtils;
import com.cherry.common.flex.core.page.PageQuery;
import com.cherry.common.flex.core.page.TableDataInfo;
import com.cherry.common.flex.utils.SearchUtils;
import com.cherry.common.satoken.utils.LoginHelper;
import com.cherry.system.domain.SysRole;
import com.cherry.system.domain.SysUserRole;
import com.cherry.system.domain.bo.SysRoleBo;
import com.cherry.system.domain.vo.SysRoleVo;
import com.cherry.system.mapper.SysRoleMapper;
import com.cherry.system.mapper.SysUserRoleMapper;
import com.cherry.system.service.ISysRoleService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 角色 业务层处理
 *
 * @author keer
 * @date 2025-05-27
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole>
    implements ISysRoleService {
  // todo

  private final SysRoleMapper roleMapper;
  private final SysUserRoleMapper userRoleMapper;

  /**
   * 根据条件分页查询角色数据
   *
   * @param role 角色信息
   * @return 角色数据集合信息
   */
  @Override
  public TableDataInfo<SysRoleVo> selectPageRoleList(SysRoleBo role, PageQuery pageQuery) {
    QueryWrapper qw = buildQueryWrapper(role, pageQuery);
    pageQuery.buildOrders(qw);
    Page<SysRoleVo> page = this.pageAs(pageQuery.build(), qw, SysRoleVo.class);
    return TableDataInfo.build(page);
  }

  /**
   * 根据条件查询角色数据
   *
   * @param role 角色信息
   * @return 角色数据集合信息
   */
  @Override
  public List<SysRoleVo> selectRoleList(SysRoleBo role) {
    QueryWrapper qw = buildQueryWrapper(role, null);
    return this.listAs(qw, SysRoleVo.class);
  }

  /**
   * 查询登录用户所属的角色
   *
   * @param role 角色信息
   * @return 角色数据集合信息
   */
  @Override
  public List<SysRoleVo> selectRoleListByLoginUser(SysRoleBo role) {
    QueryWrapper qw = buildQueryWrapper(role, null);
    List<String> loginUserRoleIds =
        StreamUtils.toList(
            userRoleMapper.selectListByQuery(
                QueryWrapper.create().eq(SysUserRole::getUserId, LoginHelper.getUserId())),
            SysUserRole::getRoleId);
    if(CollUtil.isNotEmpty(loginUserRoleIds)) {
        qw.in(SysRole::getId, loginUserRoleIds);
    }
    return this.listAs(qw, SysRoleVo.class);
  }

  private QueryWrapper buildQueryWrapper(SysRoleBo bo, PageQuery pageQuery) {
    Map<String, Object> params = bo.getParams();
    QueryWrapper qw = new QueryWrapper();
    qw.create()
        .select()
        .from(SysRole.class)
        .eq(SysRole::getId, bo.getId())
        .like(SysRole::getRoleName, bo.getRoleName())
        .eq(SysRole::getStatus, bo.getStatus())
        .like(SysRole::getRoleKey, bo.getRoleKey());

    SearchUtils.buildTimeBetween(qw, "create_time", params.get("beginTime"), params.get("endTime"));

    if (ObjUtil.isNotNull(pageQuery) && ObjUtil.isNull(pageQuery.getOrderByColumn())) {
      qw.orderBy(SysRole::getRoleSort, true);
    }
    return qw;
  }

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
    List<SysRoleVo> list =
        MapstructUtils.convert(roleMapper.selectRolesByUserId(userId), SysRoleVo.class);
    return list;
  }
}
