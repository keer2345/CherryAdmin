package com.cherry.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.cherry.common.core.constant.SystemConstants;
import com.cherry.common.core.utils.StreamUtils;
import com.cherry.common.core.utils.StringUtils;
import com.cherry.common.satoken.handler.LoginHelper;
import com.cherry.system.domain.SysMenu;
import com.cherry.system.domain.vo.MetaVo;
import com.cherry.system.domain.vo.RouterVo;
import com.cherry.system.mapper.SysMenuMapper;
import com.cherry.system.service.ISysMenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

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

  /**
   * 根据用户ID查询菜单
   *
   * @param userId 用户名称
   * @return 菜单列表
   */
  @Override
  public List<SysMenu> selectMenuTreeByUserId(Long userId) {
    List<SysMenu> menus;
    if (LoginHelper.isSuperAdmin(userId)) {
      menus = baseMapper.selectMenuTreeAll();
    } else {
      menus = baseMapper.selectMenuTreeByUserId(userId);
    }
    return getChildPerms(menus, 0);
  }

  /**
   * 构建前端路由所需要的菜单 路由name命名规则 path首字母转大写 + id
   *
   * @param menus 菜单列表
   * @return 路由列表
   */
  @Override
  public List<RouterVo> buildMenus(List<SysMenu> menus) {
    List<RouterVo> routers = new LinkedList<>();
    for (SysMenu menu : menus) {
      String name = menu.getRouteName() + menu.getMenuId();
      RouterVo router = new RouterVo();
      router.setHidden("1".equals(menu.getVisible()));
      router.setName(name);
      router.setPath(menu.getRouterPath());
      router.setComponent(menu.getComponentInfo());
      router.setQuery(menu.getQueryParam());
      router.setMeta(
          new MetaVo(
              menu.getMenuName(),
              menu.getIcon(),
              StringUtils.equals("1", menu.getIsCache()),
              menu.getPath()));
      List<SysMenu> cMenus = menu.getChildren();
      if (CollUtil.isNotEmpty(cMenus) && SystemConstants.TYPE_DIR.equals(menu.getMenuType())) {
        router.setAlwaysShow(true);
        router.setRedirect("noRedirect");
        router.setChildren(buildMenus(cMenus));
      } else if (menu.isMenuFrame()) {
        String frameName = StringUtils.capitalize(menu.getPath()) + menu.getMenuId();
        router.setMeta(null);
        List<RouterVo> childrenList = new ArrayList<>();
        RouterVo children = new RouterVo();
        children.setPath(menu.getPath());
        children.setComponent(menu.getComponent());
        children.setName(frameName);
        children.setMeta(
            new MetaVo(
                menu.getMenuName(),
                menu.getIcon(),
                StringUtils.equals("1", menu.getIsCache()),
                menu.getPath()));
        children.setQuery(menu.getQueryParam());
        childrenList.add(children);
        router.setChildren(childrenList);
      } else if (menu.getParentId().intValue() == 0 && menu.isInnerLink()) {
        router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon()));
        router.setPath("/");
        List<RouterVo> childrenList = new ArrayList<>();
        RouterVo children = new RouterVo();
        String routerPath = SysMenu.innerLinkReplaceEach(menu.getPath());
        String innerLinkName = StringUtils.capitalize(routerPath) + menu.getMenuId();
        children.setPath(routerPath);
        children.setComponent(SystemConstants.INNER_LINK);
        children.setName(innerLinkName);
        children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), menu.getPath()));
        childrenList.add(children);
        router.setChildren(childrenList);
      }
      routers.add(router);
    }
    return routers;
  }

  /**
   * 根据父节点的ID获取所有子节点
   *
   * @param list 分类表
   * @param parentId 传入的父节点ID
   * @return String
   */
  private List<SysMenu> getChildPerms(List<SysMenu> list, int parentId) {
    List<SysMenu> returnList = new ArrayList<>();
    for (SysMenu t : list) {
      // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
      if (t.getParentId() == parentId) {
        recursionFn(list, t);
        returnList.add(t);
      }
    }
    return returnList;
  }

  /** 递归列表 */
  private void recursionFn(List<SysMenu> list, SysMenu t) {
    // 得到子节点列表
    List<SysMenu> childList = StreamUtils.filter(list, n -> n.getParentId().equals(t.getMenuId()));
    t.setChildren(childList);
    for (SysMenu tChild : childList) {
      // 判断是否有子节点
      if (list.stream().anyMatch(n -> n.getParentId().equals(tChild.getMenuId()))) {
        recursionFn(list, tChild);
      }
    }
  }
}
