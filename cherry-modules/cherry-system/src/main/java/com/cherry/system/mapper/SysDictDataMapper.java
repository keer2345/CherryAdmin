package com.cherry.system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cherry.common.mybatis.core.mapper.BaseMapperPlus;
import com.cherry.system.domain.SysDictData;
import com.cherry.system.domain.vo.SysDictDataVo;

import java.util.List;

/**
 * 字典表 数据层
 *
 * @author keer
 * @date 2025-06-06
 */
public interface SysDictDataMapper extends BaseMapperPlus<SysDictData, SysDictDataVo> {

  /**
   * 根据字典类型查询字典数据列表
   *
   * @param dictType 字典类型
   * @return 符合条件的字典数据列表
   */
  default List<SysDictDataVo> selectDictDataByType(String dictType) {
    return selectVoList(
        new LambdaQueryWrapper<SysDictData>()
            .eq(SysDictData::getDictType, dictType)
            .orderByAsc(SysDictData::getDictSort));
  }
}
