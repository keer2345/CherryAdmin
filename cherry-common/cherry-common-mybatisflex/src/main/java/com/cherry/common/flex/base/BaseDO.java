package com.cherry.common.flex.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 基础实体对象
 *
 * @author keer2345
 * @date 2025-06-24
 */
@Data
public class BaseDO {
  // todo

  @Id(keyType = KeyType.Generator, value = "uuid")
  private String id;

  /** 创建时间 */
  @Column(onInsertValue = "now()")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date createTime;

  /** 最后更新时间 */
//  @Column(onUpdateValue = "now()", onInsertValue = "now()")
  private Date updateTime;

  /**
   * 创建者，目前使用 SysUser 的 id 编号
   *
   * <p>使用 String 类型的原因是，未来可能会存在非数值的情况，留好拓展性。
   */
  //  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private String creator;

    /**
     * 创建者部门ID，目前使用 SysUser 的 deptId 编号
     */
    private String createDept;

  /**
   * 更新者，目前使用 SysUser 的 id 编号
   *
   * <p>使用 String 类型的原因是，未来可能会存在非数值的情况，留好拓展性。
   */
  //  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private String updater;

  /** 删除人ID */
  private String deleteId;

  /** 删除时间 */
//  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date deleteTime;

  /**
   * 是否删除
   *
   * <p>false: normal, true: deleted
   */
  @Column(isLogicDelete = true)
  private Boolean deleted;

  /** 把 creator、createTime、updateTime、updater 都清空，避免前端直接传递 creator 之类的字段，直接就被更新了 */
  public void clean() {
    this.creator = null;
    this.createTime = null;
    this.updater = null;
    this.updateTime = null;
    this.deleteId = null;
    this.deleteTime = null;
  }
}
