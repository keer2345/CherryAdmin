package com.cherry.system.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cherry.common.mybatis.annotation.DataColumn;
import com.cherry.common.mybatis.annotation.DataPermission;
import com.cherry.common.mybatis.core.mapper.BaseMapperPlus;
import com.cherry.system.domain.SysUser;
import com.cherry.system.domain.vo.SysUserVo;
import org.apache.ibatis.annotations.Param;

/**
 * 用户表 数据层
 *
 * @author keer
 * @date 2025-05-26
 */
public interface SysUserMapper extends BaseMapperPlus<SysUser, SysUserVo> {
  // todo

  /**
   * 分页查询用户列表，并进行数据权限控制
   *
   * @param page 分页参数
   * @param queryWrapper 查询条件
   * @return 分页的用户信息
   */
  @DataPermission({
    @DataColumn(key = "deptName", value = "u.dept_id"),
    @DataColumn(key = "userName", value = "u.user_id")
  })
  Page<SysUserVo> selectPageUserList(
      @Param("page") Page<SysUser> page, @Param(Constants.WRAPPER) Wrapper<SysUser> queryWrapper);
}
