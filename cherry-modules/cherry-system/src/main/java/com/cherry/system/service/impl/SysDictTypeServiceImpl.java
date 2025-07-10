package com.cherry.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.cherry.common.core.constant.CacheNames;
import com.cherry.common.core.service.DictService;
import com.cherry.common.core.utils.MapstructUtils;
import com.cherry.common.core.utils.SpringUtils;
import com.cherry.common.core.utils.StreamUtils;
import com.cherry.common.core.utils.StringUtils;
import com.cherry.system.domain.SysDictData;
import com.cherry.system.domain.SysDictType;
import com.cherry.system.domain.vo.SysDictDataVo;
import com.cherry.system.mapper.SysDictDataMapper;
import com.cherry.system.mapper.SysDictTypeMapper;
import com.cherry.system.service.ISysDictTypeService;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 字典 业务层处理
 *
 * @author keer
 * @date 2025-06-06
 */
@RequiredArgsConstructor
@Service
public class SysDictTypeServiceImpl extends ServiceImpl<SysDictTypeMapper, SysDictType>
    implements ISysDictTypeService, DictService {
  // todo

  private final SysDictDataMapper dictDataMapper;

  /**
   * 根据字典类型查询信息
   *
   * @param dictType 字典类型
   * @return 字典类型
   */
  @Cacheable(cacheNames = CacheNames.SYS_DICT, key = "#dictType")
  @Override
  public List<SysDictDataVo> selectDictDataByType(String dictType) {
    List<SysDictDataVo> dictData =
        MapstructUtils.convert(
            dictDataMapper.selectListByQuery(
                new QueryWrapper()
                    .eq(SysDictData::getDictType, dictType)
                    .orderBy(SysDictData::getDictSort, true)),
            SysDictDataVo.class);
    if (CollUtil.isNotEmpty(dictData)) {
      return dictData;
    }
    return null;
  }

  /**
   * 根据字典类型和字典值获取字典标签
   *
   * @param dictType 字典类型
   * @param dictValue 字典值
   * @param separator 分隔符
   * @return 字典标签
   */
  @Override
  public String getDictLabel(String dictType, String dictValue, String separator) {
    List<SysDictDataVo> datas = SpringUtils.getAopProxy(this).selectDictDataByType(dictType);
    Map<String, String> map =
        StreamUtils.toMap(datas, SysDictDataVo::getDictValue, SysDictDataVo::getDictLabel);
    if (StringUtils.containsAny(dictValue, separator)) {
      return Arrays.stream(dictValue.split(separator))
          .map(v -> map.getOrDefault(v, StringUtils.EMPTY))
          .collect(Collectors.joining(separator));
    } else {
      return map.getOrDefault(dictValue, StringUtils.EMPTY);
    }
  }
}
