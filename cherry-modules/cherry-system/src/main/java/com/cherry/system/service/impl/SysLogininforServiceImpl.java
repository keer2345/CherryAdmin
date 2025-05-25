package com.cherry.system.service.impl;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.cherry.common.core.constant.Constants;
import com.cherry.common.core.utils.MapstructUtils;
import com.cherry.common.core.utils.ServletUtils;
import com.cherry.common.core.utils.StringUtils;
import com.cherry.common.log.event.LogininforEvent;
import com.cherry.common.satoken.handler.LoginHelper;
import com.cherry.system.domain.SysLogininfor;
import com.cherry.system.domain.bo.SysLogininforBo;
import com.cherry.system.domain.vo.SysClientVo;
import com.cherry.system.mapper.SysLogininforMapper;
import com.cherry.system.service.ISysClientService;
import com.cherry.system.service.ISysLogininforService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 系统访问日志情况信息 服务层处理
 *
 * @author keer
 * @date 2025-05-24
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class SysLogininforServiceImpl implements ISysLogininforService {
  // todo
  private final SysLogininforMapper baseMapper;
  private final ISysClientService clientService;

  /**
   * 记录登录信息
   *
   * @param logininforEvent 登录事件
   */
  @Async
  @EventListener
  public void recordLogininfor(LogininforEvent logininforEvent) {
    HttpServletRequest request = logininforEvent.getRequest();
    final UserAgent userAgent = UserAgentUtil.parse(request.getHeader("User-Agent"));
    final String ip = ServletUtils.getClientIP(request);
    // 客户端信息
    String clientId = request.getHeader(LoginHelper.CLIENT_KEY);
    SysClientVo client = null;
    if (StringUtils.isNotBlank(clientId)) {
      client = clientService.queryByClientId(clientId);
    }

    StringBuilder s = new StringBuilder();
    s.append(getBlock(ip));
    s.append(getBlock(logininforEvent.getUsername()));
    s.append(getBlock(logininforEvent.getStatus()));
    s.append(getBlock(logininforEvent.getMessage()));

    // 获取客户端操作系统
    String os = userAgent.getOs().getName();
    // 获取客户端浏览器
    String browser = userAgent.getBrowser().getName();
    // 封装对象
    SysLogininforBo logininfor = new SysLogininforBo();
    logininfor.setTenantId(logininforEvent.getTenantId());
    logininfor.setUserName(logininforEvent.getUsername());
    if (ObjUtil.isNotNull(client)) {
      logininfor.setClientKey(client.getClientKey());
      logininfor.setDeviceType(client.getDeviceType());
    }
    logininfor.setIpaddr(ip);
    logininfor.setBrowser(browser);
    logininfor.setOs(os);
    logininfor.setMsg(logininforEvent.getMessage());

    // todo
    // String address = AddressUtils.getRealAddressByIP(ip);
    // s.append(address);
    // logininfor.setLoginLocation(address);

    // 打印信息到日志
    log.info("{} {}", s.toString(), logininforEvent.getArgs());

    // 日志状态
    if (StringUtils.equalsAny(
        logininforEvent.getStatus(),
        Constants.LOGIN_SUCCESS,
        Constants.LOGOUT,
        Constants.REGISTER)) {
      logininfor.setStatus(Constants.SUCCESS);
    } else if (Constants.LOGIN_FAIL.equals(logininforEvent.getStatus())) {
      logininfor.setStatus(Constants.FAIL);
    }
    // 插入数据
    insertLogininfor(logininfor);
  }

  private String getBlock(Object msg) {
    if (msg == null) {
      msg = "";
    }
    return "[" + msg.toString() + "]";
  }

  /**
   * 新增系统登录日志
   *
   * @param bo 访问日志对象
   */
  @Override
  public void insertLogininfor(SysLogininforBo bo) {
    SysLogininfor logininfor = MapstructUtils.convert(bo, SysLogininfor.class);
    logininfor.setLoginTime(new Date());
    int result = baseMapper.insert(logininfor);
  }
}
