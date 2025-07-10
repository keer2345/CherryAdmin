package com.cherry.system.mapper;

import com.cherry.system.domain.SysOperLog;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 操作日志 数据层
 *
 * @author keer
 * @date 2025-06-06
 */
@Mapper
public interface SysOperLogMapper extends BaseMapper<SysOperLog> {}
