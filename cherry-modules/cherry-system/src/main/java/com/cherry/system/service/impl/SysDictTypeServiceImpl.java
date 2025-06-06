package com.cherry.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.cherry.system.domain.vo.SysDictDataVo;
import com.cherry.system.mapper.SysDictDataMapper;
import com.cherry.system.service.ISysDictTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 字典 业务层处理
 *
 * @author keer
 * @date 2025-06-06
 */
@RequiredArgsConstructor
@Service
public class SysDictTypeServiceImpl implements ISysDictTypeService {
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
}
