package com.cherry.system.mapper;

import static com.mybatisflex.core.query.QueryMethods.*;

import com.cherry.system.domain.SysDept;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 部门管理 数据层
 *
 * @author keer
 * @date 2025-05-28
 */
@Mapper
public interface SysDeptMapper extends BaseMapper<SysDept> {}
