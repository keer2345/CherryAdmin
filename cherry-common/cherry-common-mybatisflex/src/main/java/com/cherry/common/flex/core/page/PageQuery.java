package com.cherry.common.flex.core.page;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.cherry.common.core.exception.ServiceException;
import com.cherry.common.core.utils.StringUtils;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.util.SqlUtil;
import java.io.Serial;
import java.io.Serializable;
import lombok.Data;

/**
 * 分页查询实体类
 *
 * @author keer2345
 * @date 2025-07-08
 */
@Data
public class PageQuery implements Serializable {
  // todo

  @Serial private static final long serialVersionUID = 1L;

  /** 分页大小 */
  private Integer pageSize;

  /** 当前页数 */
  private Integer pageNum;

  /** 排序列 */
  private String orderByColumn;

  /** 排序的方向desc或者asc */
  private String isAsc;

  /** 当前记录起始索引 默认值 */
  public static final int DEFAULT_PAGE_NUM = 1;

  /** 每页显示记录数 默认值 默认查全部 */
  public static final int DEFAULT_PAGE_SIZE = Integer.MAX_VALUE;

  /** 构建分页对象 */
  public <T> Page<T> build() {
    Integer pageNum = ObjUtil.defaultIfNull(getPageNum(), DEFAULT_PAGE_NUM);
    Integer pageSize = ObjUtil.defaultIfNull(getPageSize(), DEFAULT_PAGE_SIZE);

    if (pageNum <= 0) {
      pageNum = DEFAULT_PAGE_NUM;
    }

    Page<T> page = new Page<T>(pageNum, pageSize);
    return page;
  }

  /***
   * 构建排序
   *
   * 支持的用法如下:
   * {isAsc:"asc",orderByColumn:"id"} order by id asc
   * {isAsc:"asc",orderByColumn:"id,createTime"} order by id asc,create_time asc
   * {isAsc:"desc",orderByColumn:"id,createTime"} order by id desc,create_time desc
   * {isAsc:"asc,desc",orderByColumn:"id,createTime"} order by id asc,create_time desc
   */
  public QueryWrapper buildOrders(QueryWrapper queryWrapper) {
    if (StrUtil.isBlank(orderByColumn) || StrUtil.isBlank(isAsc)) {
      return queryWrapper.orderBy("create_time", false);
    }
    SqlUtil.keepOrderBySqlSafely(orderByColumn);
    String orderBy = StrUtil.toUnderlineCase(orderByColumn);

    // 兼容前端排序类型
    isAsc =
        StringUtils.replaceEach(
            isAsc, new String[] {"ascending", "descending"}, new String[] {"asc", "desc"});

    String[] orderByArr = orderBy.split(StringUtils.SEPARATOR);
    String[] isAscArr = isAsc.split(StringUtils.SEPARATOR);

    if (isAscArr.length != 1 && isAscArr.length != orderByArr.length) {
      throw new ServiceException("排序参数有误");
    }

    // 每个字段各自排序
    for (int i = 0; i < orderByArr.length; i++) {
      String orderByStr = orderByArr[i];
      String isAscStr = isAscArr.length == 1 ? isAscArr[0] : isAscArr[i];
      if (!StrUtil.equals(isAscStr, "asc") || !StrUtil.equals(isAscStr, "desc")) {
        throw new ServiceException("排序参数有误");
      }
      Boolean asc = StrUtil.equals(isAscStr, "asc");
      queryWrapper.orderBy(orderByArr[i], asc);
    }

    return queryWrapper;
  }

  public PageQuery(Integer pageSize, Integer pageNum) {
    this.pageSize = pageSize;
    this.pageNum = pageNum;
  }
}
