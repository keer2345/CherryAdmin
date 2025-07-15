package com.cherry.system.mapper;

import com.cherry.system.domain.SysPost;
import com.cherry.system.domain.SysUser;
import com.cherry.system.domain.SysUserPost;
import com.cherry.system.domain.vo.SysPostVo;
import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.query.QueryWrapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 岗位信息 数据层
 *
 * @author keer
 * @date 2025-05-27
 */
@Mapper
public interface SysPostMapper extends BaseMapper<SysPost> {
  // todo
  /**
   * 查询用户所属岗位组
   *
   * @param userId 用户ID
   * @return 结果
   */
  default List<SysPost> selectPostsByUserId(String userId) {
    QueryWrapper queryWrapper = new QueryWrapper();
    queryWrapper
//        .create()
//        .select()
        .from(SysPost.class)
        .as("a")
        .leftJoin(SysUserPost.class)
        .on(SysPost::getId, SysUserPost::getPostId)
        .leftJoin(SysUser.class)
        .on(SysUser::getId, SysUserPost::getUserId)
        .where(SysUser::getId)
        .eq(userId);
    return this.selectListByQuery(queryWrapper);
  }
}
