package com.cherry.system.service;

import com.cherry.system.domain.vo.SysOssVo;

/**
 * 文件上传 服务层
 *
 * @author keer
 * @date 2025-06-08
 */
public interface ISysOssService {
  // todo

  /**
   * 根据 ossId 从缓存或数据库中获取 SysOssVo 对象
   *
   * @param ossId 文件在数据库中的唯一标识
   * @return SysOssVo 对象，包含文件信息
   */
  SysOssVo getById(Long ossId);
}
