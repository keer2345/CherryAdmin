package com.cherry.common.mybatis.core.mapper;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.reflect.GenericTypeUtils;
import com.cherry.common.core.utils.MapstructUtils;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;

import java.util.List;

/**
 * 自定义 Mapper 接口, 实现 自定义扩展
 *
 * @param <T> table 泛型
 * @param <V> vo 泛型
 * @author keer
 * @date 2025-05-22
 */
@SuppressWarnings("unchecked")
public interface BaseMapperPlus<T, V> extends BaseMapper<T> {
  // todo

  Log log = LogFactory.getLog(BaseMapperPlus.class);

  /**
   * 获取当前实例对象关联的泛型类型 V 的 Class 对象
   *
   * @return 返回当前实例对象关联的泛型类型 V 的 Class 对象
   */
  default Class<V> currentVoClass() {
    return (Class<V>)
        GenericTypeUtils.resolveTypeArguments(this.getClass(), BaseMapperPlus.class)[1];
  }

  /**
   * 根据条件查询VO对象列表
   *
   * @param wrapper 查询条件Wrapper
   * @return 查询到的VO对象列表
   */
  default List<V> selectVoList(Wrapper<T> wrapper) {
    return selectVoList(wrapper, this.currentVoClass());
  }

  /**
   * 根据条件查询实体对象列表，并将其转换为指定的VO对象列表
   *
   * @param wrapper 查询条件Wrapper
   * @param voClass 要转换的VO类的Class对象
   * @param <C> VO类的类型
   * @return 查询到的VO对象列表，经过转换为指定的VO类后返回
   */
  default <C> List<C> selectVoList(Wrapper<T> wrapper, Class<C> voClass) {
    List<T> list = this.selectList(wrapper);
    if (CollUtil.isEmpty(list)) {
      return CollUtil.newArrayList();
    }
    return MapstructUtils.convert(list, voClass);
  }

  /**
   * 根据条件查询单个VO对象
   *
   * @param wrapper 查询条件Wrapper
   * @return 查询到的单个VO对象
   */
  default V selectVoOne(Wrapper<T> wrapper) {
    return selectVoOne(wrapper, this.currentVoClass());
  }

  /**
   * 根据条件查询单个VO对象，并指定返回的VO对象的类型
   *
   * @param wrapper 查询条件Wrapper
   * @param voClass 返回的VO对象的Class对象
   * @param <C> 返回的VO对象的类型
   * @return 查询到的单个VO对象，经过类型转换为指定的VO类后返回
   */
  default <C> C selectVoOne(Wrapper<T> wrapper, Class<C> voClass) {
    return selectVoOne(wrapper, voClass, true);
  }

  /**
   * 根据条件查询单个实体对象，并将其转换为指定的VO对象
   *
   * @param wrapper 查询条件Wrapper
   * @param voClass 要转换的VO类的Class对象
   * @param throwEx 是否抛出异常的标志
   * @param <C> VO类的类型
   * @return 查询到的单个VO对象，经过转换为指定的VO类后返回
   */
  default <C> C selectVoOne(Wrapper<T> wrapper, Class<C> voClass, boolean throwEx) {
    T obj = this.selectOne(wrapper, throwEx);
    if (ObjectUtil.isNull(obj)) {
      return null;
    }
    return MapstructUtils.convert(obj, voClass);
  }
}
