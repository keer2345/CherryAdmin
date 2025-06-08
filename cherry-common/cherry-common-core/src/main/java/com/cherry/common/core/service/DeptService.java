package com.cherry.common.core.service;

import java.util.List;

/**
 * 通用 部门服务
 *
 * @author keer
 * @date 2025-06-08
 */
public interface DeptService {

    // todo

    /**
     * 通过部门ID查询部门名称
     *
     * @param deptIds 部门ID串逗号分隔
     * @return 部门名称串逗号分隔
     */
    String selectDeptNameByIds(String deptIds);

}
