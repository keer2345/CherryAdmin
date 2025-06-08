package com.cherry.system.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.cherry.common.core.constant.CacheNames;
import com.cherry.common.core.service.OssService;
import com.cherry.common.core.utils.SpringUtils;
import com.cherry.common.core.utils.StringUtils;
import com.cherry.system.domain.vo.SysOssVo;
import com.cherry.system.mapper.SysOssMapper;
import com.cherry.system.service.ISysOssService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件上传 服务层实现
 *
 * @author Lion Li
 */
@RequiredArgsConstructor
@Service
public class SysOssServiceImpl implements ISysOssService, OssService {
  // todo

  private final SysOssMapper baseMapper;

  /**
   * 根据 ossId 从缓存或数据库中获取 SysOssVo 对象
   *
   * @param ossId 文件在数据库中的唯一标识
   * @return SysOssVo 对象，包含文件信息
   */
  @Cacheable(cacheNames = CacheNames.SYS_OSS, key = "#ossId")
  @Override
  public SysOssVo getById(Long ossId) {
    return baseMapper.selectVoById(ossId);
  }

  /**
   * 根据一组 ossIds 获取对应文件的 URL 列表
   *
   * @param ossIds 以逗号分隔的 ossId 字符串
   * @return 以逗号分隔的文件 URL 字符串
   */
  @Override
  public String selectUrlByIds(String ossIds) {
    List<String> list = new ArrayList<>();
    SysOssServiceImpl ossService = SpringUtils.getAopProxy(this);
    for (Long id : StringUtils.splitTo(ossIds, Convert::toLong)) {
      SysOssVo vo = ossService.getById(id);
      if (ObjectUtil.isNotNull(vo)) {
        try {
          // todo
          list.add(this.matchingUrl(vo).getUrl());
        } catch (Exception ignored) {
          // 如果oss异常无法连接则将数据直接返回
          list.add(vo.getUrl());
        }
      }
    }
    return String.join(StringUtils.SEPARATOR, list);
  }

  /**
   * 桶类型为 private 的URL 修改为临时URL时长为120s
   *
   * @param oss OSS对象
   * @return oss 匹配Url的OSS对象
   */
  private SysOssVo matchingUrl(SysOssVo oss) {
    // todo

    // OssClient storage = OssFactory.instance(oss.getService());
    // // 仅修改桶类型为 private 的URL，临时URL时长为120s
    // if (AccessPolicyType.PRIVATE == storage.getAccessPolicy()) {
    //   oss.setUrl(storage.getPrivateUrl(oss.getFileName(), Duration.ofSeconds(120)));
    // }
    return oss;
  }
}
