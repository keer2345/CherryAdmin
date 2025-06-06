package com.cherry.system.controller.monitor;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.cherry.common.core.constant.CacheConstants;
import com.cherry.common.core.domain.R;
import com.cherry.common.core.domain.dto.UserOnlineDTO;
import com.cherry.common.log.annotation.Log;
import com.cherry.common.log.enums.BusinessType;
import com.cherry.common.mybatis.core.page.TableDataInfo;
import com.cherry.common.redis.utils.RedisUtils;
import com.cherry.common.web.core.BaseController;
import com.cherry.system.domain.SysUserOnline;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 在线用户监控
 *
 * @author keer
 * @date 2025-06-06
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/monitor/online")
public class SysUserOnlineController extends BaseController {

  /** 获取当前用户登录在线设备 */
  @GetMapping()
  public TableDataInfo<SysUserOnline> getInfo() {
    // 获取指定账号 id 的 token 集合
    List<String> tokenIds = StpUtil.getTokenValueListByLoginId(StpUtil.getLoginIdAsString());

    List<UserOnlineDTO> userOnlineDTOList =
        tokenIds.stream()
            .filter(token -> StpUtil.stpLogic.getTokenActiveTimeoutByToken(token) >= -1)
            .map(
                token ->
                    (UserOnlineDTO)
                        RedisUtils.getCacheObject(CacheConstants.ONLINE_TOKEN_KEY + token))
            .collect(Collectors.toList());

    // 复制和处理 SysUserOnline 对象列表
    Collections.reverse(userOnlineDTOList);
    userOnlineDTOList.removeAll(Collections.singleton(null));

    List<SysUserOnline> userOnlineList =
        BeanUtil.copyToList(userOnlineDTOList, SysUserOnline.class);
    return TableDataInfo.build(userOnlineList);
  }

    /**
     * 强退当前在线设备
     *
     * @param tokenId token值
     */
    @Log(title = "在线设备", businessType = BusinessType.FORCE)
    @DeleteMapping("/myself/{tokenId}")
    public R<Void> remove(@PathVariable("tokenId") String tokenId) throws InterruptedException {
        try {
            // 获取指定账号 id 的 token 集合
            List<String> keys = StpUtil.getTokenValueListByLoginId(StpUtil.getLoginIdAsString());
            keys.stream()
                .filter(key -> key.equals(tokenId))
                .findFirst()
                .ifPresent(key -> StpUtil.kickoutByTokenValue(tokenId));
        } catch (NotLoginException ignored) {
        }
        return R.ok();
    }
}
