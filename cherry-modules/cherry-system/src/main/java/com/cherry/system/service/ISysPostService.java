package com.cherry.system.service;

import com.cherry.system.domain.vo.SysPostVo;

import java.util.List;

/**
 * 岗位信息 服务层
 *
 * @author keer
 * @date 2025-05-27
 */
public interface ISysPostService {
    // todo
    /**
     * 查询用户所属岗位组
     *
     * @param userId 用户ID
     * @return 岗位ID
     */
    List<SysPostVo> selectPostsByUserId(Long userId);
}
