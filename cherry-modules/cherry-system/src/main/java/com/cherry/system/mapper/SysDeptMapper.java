package com.cherry.system.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.cherry.common.mybatis.annotation.DataColumn;
import com.cherry.common.mybatis.annotation.DataPermission;
import com.cherry.common.mybatis.core.mapper.BaseMapperPlus;
import com.cherry.common.mybatis.helper.DataBaseHelper;
import com.cherry.system.domain.SysDept;
import com.cherry.system.domain.vo.SysDeptVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 部门管理 数据层
 *
 * @author keer
 * @date 2025-05-28
 */
public interface SysDeptMapper extends BaseMapperPlus<SysDept, SysDeptVo> {
  // todo

  /**
   * 根据父部门ID查询其所有子部门的列表
   *
   * @param parentId 父部门ID
   * @return 包含子部门的列表
   */
  default List<SysDept> selectListByParentId(Long parentId) {
    return this.selectList(
        new LambdaQueryWrapper<SysDept>()
            .select(SysDept::getDeptId)
            .apply(DataBaseHelper.findInSet(parentId, "ancestors")));
  }

    /**
     * 查询部门管理数据
     *
     * @param queryWrapper 查询条件
     * @return 部门信息集合
     */
    @DataPermission({
        @DataColumn(key = "deptName", value = "dept_id")
    })
    List<SysDeptVo> selectDeptList(@Param(Constants.WRAPPER) Wrapper<SysDept> queryWrapper);
}
