# Mybatis-flex

- https://blog.csdn.net/weixin_42588555/article/details/148908399
- https://blog.csdn.net/weixin_54881347/article/details/142379334
- https://blog.csdn.net/weixin_54881347/article/details/142620675

## 权限控制

### 数据权限

1. 所有权限 ALL
2. 自定义（就是指定该角色包含哪些部门） CUSTOM
3. 本部门 DEPT
4. 本部门及下属部门 DEPT_AND_CHILD
5. 仅本人 SELF

#### 用户与角色

SysUserRole

 userId | roleId |
--------|--------|
 1      | 1      |
 1      | 2      |
 1      | 3      |

#### 角色与菜单

SysRoleMenu

 roleId | menuId |
--------|--------|
 1      | 1      |
 1      | 2      |
 1      | 3      |

#### 角色与数据权限

角色对应哪些数据权限

 roleId | permId |
--------|--------|
 1      | 1      |
 1      | 2      |
 1      | 3      |

#### 角色与部门

SysRoleDept

 roleId | deptId |
--------|--------|
 1      | 1      |
 1      | 2      |
 1      | 3      |

### 判断逻辑
如果该角色有“1-所有权限“，则不做处理。

