package com.cherry.system.mapper;

import com.cherry.system.domain.SysDept;
import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.query.QueryWrapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 部门管理 数据层
 *
 * @author keer
 * @date 2025-05-28
 */
@Mapper
public interface SysDeptMapper extends BaseMapper<SysDept> {}
