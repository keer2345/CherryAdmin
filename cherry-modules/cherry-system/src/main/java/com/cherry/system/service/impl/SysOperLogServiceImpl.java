package com.cherry.system.service.impl;

import cn.hutool.core.util.ArrayUtil;
import com.cherry.common.core.utils.MapstructUtils;
import com.cherry.common.core.utils.StringUtils;
import com.cherry.common.core.utils.ip.AddressUtils;
import com.cherry.common.flex.utils.SearchUtils;
import com.cherry.common.flex.core.page.PageQuery;
import com.cherry.common.flex.core.page.TableDataInfo;
import com.cherry.common.log.event.OperLogEvent;
import com.cherry.system.domain.SysOperLog;
import com.cherry.system.domain.bo.SysOperLogBo;
import com.cherry.system.domain.vo.SysOperLogVo;
import com.cherry.system.mapper.SysOperLogMapper;
import com.cherry.system.service.ISysOperLogService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 操作日志 服务层处理
 *
 * @author keer
 * @date 2025-06-06
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class SysOperLogServiceImpl extends ServiceImpl<SysOperLogMapper, SysOperLog>
    implements ISysOperLogService {

  private final SysOperLogMapper operLogMapper;

  /**
   * 操作日志记录
   *
   * @param operLogEvent 操作日志事件
   */
  @Async
  @EventListener
  public void recordOper(OperLogEvent operLogEvent) {
    SysOperLogBo operLog = MapstructUtils.convert(operLogEvent, SysOperLogBo.class);
    // 远程查询操作地点
    operLog.setOperLocation(AddressUtils.getRealAddressByIP(operLog.getOperIp()));
    insertOperlog(operLog);
  }

  @Override
  public TableDataInfo<SysOperLogVo> selectPageOperLogList(
      SysOperLogBo operLog, PageQuery pageQuery) {
    QueryWrapper qw = buildQueryWrapper(operLog);
    pageQuery.buildOrders(qw);
    //    Page<SysOperLog> page = operLogMapper.paginate(pageQuery.build(), qw);
    //    return TableDataInfo.build(page, SysOperLogVo.class);
    Page<SysOperLogVo> page = this.pageAs(pageQuery.build(), qw, SysOperLogVo.class);
    return TableDataInfo.build(page);
  }

  private QueryWrapper buildQueryWrapper(SysOperLogBo bo) {
    Map<String, Object> params = bo.getParams();
    return new QueryWrapper()
        .create()
        .select()
        .from(SysOperLog.class)
        .like(SysOperLog::getOperIp, bo.getOperIp())
        .like(SysOperLog::getTitle, bo.getTitle())
        .eq(
            SysOperLog::getBusinessType,
            bo.getBusinessType(),
            bo.getBusinessType() != null && bo.getBusinessType() > 0)
        .in(
            SysOperLog::getBusinessType,
            Arrays.asList(bo.getBusinessTypes()),
            ArrayUtil.isNotEmpty(bo.getBusinessTypes()))
        //        .func(
        //            f -> {
        //              if (ArrayUtil.isNotEmpty(operLog.getBusinessTypes())) {
        //                f.in(SysOperLog::getBusinessType,
        // Arrays.asList(operLog.getBusinessTypes()));
        //              }
        //            })
        .eq(SysOperLog::getStatus, bo.getStatus(), bo.getStatus() != null)
        .like(SysOperLog::getOperName, bo.getOperName(), StringUtils.isNotBlank(bo.getOperName()))
        .between(
            SysOperLog::getCreateTime,
            SearchUtils.strToDayStart(params.get("beginTime")),
            SearchUtils.strToDayStart(params.get("endTime")),
            params.get("beginTime") != null && params.get("endTime") != null);
  }

  /**
   * 新增操作日志
   *
   * @param bo 操作日志对象
   */
  @Override
  public void insertOperlog(SysOperLogBo bo) {
    SysOperLog operLog = MapstructUtils.convert(bo, SysOperLog.class);
    //        operLog.setCreateTime(LocalDateTime.now());
    //    operLog.setCreateTime(new Date());
    operLogMapper.insert(operLog);
  }

  /**
   * 查询系统操作日志集合
   *
   * @param operLog 操作日志对象
   * @return 操作日志集合
   */
  @Override
  public List<SysOperLogVo> selectOperLogList(SysOperLogBo operLog) {
    QueryWrapper qw = buildQueryWrapper(operLog);
    qw.orderBy(SysOperLog::getId, false);
    return MapstructUtils.convert(operLogMapper.selectListByQuery(qw), SysOperLogVo.class);
  }

  /**
   * 批量删除系统操作日志
   *
   * @param operIds 需要删除的操作日志ID
   * @return 结果
   */
  @Override
  public int deleteOperLogByIds(String[] operIds) {
    return operLogMapper.deleteBatchByIds(Arrays.asList(operIds));
  }

  /**
   * 查询操作日志详细
   *
   * @param operId 操作ID
   * @return 操作日志对象
   */
  @Override
  public SysOperLogVo selectOperLogById(String operId) {
    return MapstructUtils.convert(this.getById(operId), SysOperLogVo.class);
  }

  /** 清空操作日志 */
  @Override
  public void cleanOperLog() {
    //    baseMapper.delete(new LambdaQueryWrapper<>());
    operLogMapper.deleteByQuery(new QueryWrapper());
  }
}
