package com.cherry.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.cherry.common.core.service.DictService;
import com.cherry.common.core.utils.SpringUtils;
import com.cherry.common.core.utils.StreamUtils;
import com.cherry.common.core.utils.StringUtils;
import com.cherry.system.domain.vo.SysDictDataVo;
import com.cherry.system.mapper.SysDictDataMapper;
import com.cherry.system.service.ISysDictTypeService;
import lombok.RequiredArgsConstructor;
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
public class SysDictTypeServiceImpl implements ISysDictTypeService, DictService {
  // todo

  private final SysDictDataMapper dictDataMapper;

  // todo cacheable
  @Override
  public List<SysDictDataVo> selectDictDataByType(String dictType) {
    List<SysDictDataVo> dictData = dictDataMapper.selectDictDataByType(dictType);
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
