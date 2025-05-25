package com.cherry.system.service;

import com.cherry.system.domain.bo.SysLogininforBo;

/**
 * 系统访问日志情况信息 服务层
 * @author keer
 * @date 2025-05-24
 */
public interface ISysLogininforService {
  // todo

    /**
     * 新增系统登录日志
     *
     * @param bo 访问日志对象
     */
    void insertLogininfor(SysLogininforBo bo);
}
