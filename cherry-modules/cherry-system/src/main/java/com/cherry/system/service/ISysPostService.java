package com.cherry.system.service;

import com.cherry.system.domain.bo.SysPostBo;
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
    List<SysPostVo> selectPostsByUserId(String userId);

    /**
     * 查询岗位信息集合
     *
     * @param post 岗位信息
     * @return 岗位信息集合
     */
    List<SysPostVo> selectPostList(SysPostBo post) ;
    /**
     * 通过岗位ID串查询岗位
     *
     * @param postIds 岗位id串
     * @return 岗位列表信息
     */
    List<SysPostVo> selectPostByIds(List<String> postIds);
}
