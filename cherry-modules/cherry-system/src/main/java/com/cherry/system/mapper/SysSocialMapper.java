package com.cherry.system.mapper;

import com.cherry.system.domain.SysSocial;
import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.query.QueryWrapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 社会化关系Mapper接口
 *
 * @author keer
 * @date 2025-06-06
 */
@Mapper
public interface SysSocialMapper extends BaseMapper<SysSocial> {}
