package com.cherry.common.mybatis.core.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.cherry.common.core.utils.StringUtils;
import com.cherry.common.mybatis.core.page.PageQuery;
import com.cherry.common.mybatis.core.page.TableDataInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Entity基类
 *
 * @author keer
 * @date 2025-05-22
 */
@Data
public class BaseEntity implements Serializable {
  @Serial private static final long serialVersionUID = 1L;

  /** 搜索值 */
  @JsonIgnore
  @TableField(exist = false)
  private String searchValue;

  /** 创建部门 */
  @TableField(fill = FieldFill.INSERT)
  private String createDept;

  /** 创建者 */
  @TableField(fill = FieldFill.INSERT)
  private String createBy;

  /** 创建时间 */
  @TableField(fill = FieldFill.INSERT)
  private Date createTime;

  /** 更新者 */
  @TableField(fill = FieldFill.INSERT_UPDATE)
  private String updateBy;

  /** 更新时间 */
  @TableField(fill = FieldFill.INSERT_UPDATE)
  private Date updateTime;

  /** 请求参数 */
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  @TableField(exist = false)
  private Map<String, Object> params = new HashMap<>();



    public TableDataInfo<SysOperLogVo> selectPageOperLogList(
        SysOperLogBo operLog, PageQuery pageQuery) {
        QueryWrapper qw = buildQueryWrapper(operLog);
        if (StringUtils.isBlank(pageQuery.getOrderByColumn())) {
            lqw.orderByDesc(SysOperLog::getOperId);
        }
        Page<SysOperLogVo> page = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(page);
    }
}
