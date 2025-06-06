package com.cherry.system.controller.system;

import cn.hutool.core.util.ObjUtil;
import com.cherry.common.core.domain.R;
import com.cherry.common.web.core.BaseController;
import com.cherry.system.domain.vo.SysDictDataVo;
import com.cherry.system.service.ISysDictTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据字典信息
 *
 * @author keer
 * @date 2025-06-06
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/dict/data")
public class SysDictDataController extends BaseController {
  // todo

    private final ISysDictTypeService dictTypeService;

  /**
   * 根据字典类型查询字典数据信息
   *
   * @param dictType 字典类型
   */
  @GetMapping(value = "/type/{dictType}")
  public R<List<SysDictDataVo>> dictType(@PathVariable String dictType) {
    List<SysDictDataVo> data = dictTypeService.selectDictDataByType(dictType);
    if (ObjUtil.isNull(data)) {
      data = new ArrayList<>();
    }
    return R.ok(data);
  }
}
