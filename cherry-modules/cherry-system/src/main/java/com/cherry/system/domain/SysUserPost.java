package com.cherry.system.domain;

import com.cherry.common.flex.base.BaseDO;
import com.mybatisflex.annotation.Table;
import lombok.Data;

/**
 * 用户和岗位关联 sys_user_post
 *
 * @author keer2345
 * @date 2025-07-10
 */
@Data
@Table("sys_user_post")
public class SysUserPost extends BaseDO {

  /** 用户ID */
  private String userId;

  /** 岗位ID */
  private String postId;
}
