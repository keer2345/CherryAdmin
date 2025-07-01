package com.cherry.system.domain;

import com.cherry.common.tenant.core.TenantEntity;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * 部门表 sys_dept
 *
 * @author keer
 * @date 2025-05-28
 */
@Data
@Table("sys_dept")
public class SysDept extends TenantEntity {

  @Serial private static final long serialVersionUID = 1L;


  /** 父部门ID */
  private String parentId;

  /** 部门名称 */
  private String deptName;

  /** 部门类别编码 */
  private String deptCategory;

  /** 显示顺序 */
  private Integer orderNum;

  /** 负责人 */
  private String leader;

  /** 联系电话 */
  private String phone;

  /** 邮箱 */
  private String email;

  /** 部门状态:0正常,1停用 */
  private String status;


  /** 祖级列表 */
  private String ancestors;

  /** 子部门 */
//  @TableField(exist = false)
      @Column(ignore = true)
  private List<SysDept> children = new ArrayList<>();
}
