package com.cherry.system.service;

import com.cherry.system.domain.vo.SysDictDataVo;

import java.util.List;

/**
 * 字典 业务层
 * @author keer
 * @date 2025-06-06
 */
public interface ISysDictTypeService {
    // todo

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    List<SysDictDataVo> selectDictDataByType(String dictType);
}
