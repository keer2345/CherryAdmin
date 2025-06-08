package com.cherry.common.core.service;

/**
 * 通用 字典服务
 *
 * @author Lion Li
 */
public interface DictService {

  /** 分隔符 */
  String SEPARATOR = ",";

  /**
   * 根据字典类型和字典值获取字典标签
   *
   * @param dictType 字典类型
   * @param dictValue 字典值
   * @return 字典标签
   */
  default String getDictLabel(String dictType, String dictValue) {
    return getDictLabel(dictType, dictValue, SEPARATOR);
  }
  /**
   * 根据字典类型和字典值获取字典标签
   *
   * @param dictType  字典类型
   * @param dictValue 字典值
   * @param separator 分隔符
   * @return 字典标签
   */
  String getDictLabel(String dictType, String dictValue, String separator);
}
