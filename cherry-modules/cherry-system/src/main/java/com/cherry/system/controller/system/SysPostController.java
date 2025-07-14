package com.cherry.system.controller.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import com.cherry.common.core.domain.R;
import com.cherry.common.web.core.BaseController;
import com.cherry.system.domain.bo.SysPostBo;
import com.cherry.system.domain.vo.SysPostVo;
import com.cherry.system.service.ISysPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * SysPostController
 *
 * @author keer2345
 * @date 2025-07-14
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/post")
public class SysPostController extends BaseController {
  // todo
  private final ISysPostService postService;

  /**
   * 获取岗位选择框列表
   *
   * @param postIds 岗位ID串
   * @param deptId 部门id
   */
  @SaCheckPermission("system:post:query")
  @GetMapping("/optionselect")
  public R<List<SysPostVo>> optionselect(
      @RequestParam(required = false) String[] postIds,
      @RequestParam(required = false) String deptId) {
    List<SysPostVo> list = new ArrayList<>();
    if (ObjUtil.isNotNull(deptId)) {
      SysPostBo post = new SysPostBo();
      post.setDeptId(deptId);
      list = postService.selectPostList(post);
    } else if (postIds != null) {
      list = postService.selectPostByIds(List.of(postIds));
    }
    return R.ok(list);
  }
}
