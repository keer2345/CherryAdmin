package com.cherry.system.mapper;

import com.cherry.system.domain.SysUserPost;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户与岗位关联表 数据层
 *
 * @author keer2345
 * @date 2025-07-10
 */
@Mapper
public interface SysUserPostMapper extends BaseMapper<SysUserPost> {}
