package com.cherry.common.flex.permission;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.cherry.common.core.domain.dto.RoleDTO;
import com.cherry.common.core.domain.model.LoginUser;
import com.cherry.common.core.exception.ServiceException;
import com.cherry.common.core.utils.SpringUtils;
import com.cherry.common.core.utils.StreamUtils;
import com.cherry.common.core.utils.StringUtils;
import com.cherry.common.flex.enums.DataScopeType;
import com.cherry.common.satoken.utils.LoginHelper;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

/**
 * 数据权限处理器
 *
 * <p>* @see <a
 * href="https://gitee.com/dromara/RuoYi-Vue-Plus/blob/5.X/ruoyi-common/ruoyi-common-mybatis/src/main/java/org/dromara/common/mybatis/handler/PlusDataPermissionHandler.java">参考</a>
 *
 * <p>https://gitee.com/ding-wanren/base-vue-flex/blob/master/ruoyi-common/ruoyi-common-mybatis/src/main/java/org/dromara/common/mybatis/handler/PlusDataPermissionHandler.java
 *
 * @author keer2345
 * @date 2025-07-15
 */
@Slf4j
@Component
public class PlusDataPermissionHandler {
  // todo
  /** spel 解析器 */
  private final ExpressionParser parser = new SpelExpressionParser();

  private final ParserContext parserContext = new TemplateParserContext();

  /** bean解析器 用于处理 spel 表达式中对 bean 的调用 */
  private final BeanResolver beanResolver = new BeanFactoryResolver(SpringUtils.getBeanFactory());

  public void handlerDataPermission(
      DataPermission dataPermission, QueryWrapper queryWrapper, boolean isSelect) {
    if (dataPermission == null) {
      return;
    }
    DataColumn[] dataColumns = dataPermission.getValue();
    if (ArrayUtil.isEmpty(dataColumns)) {
      return;
    }
    LoginUser currentUser = DataPermissionHelper.getVariable("user");
    if (ObjectUtil.isNull(currentUser)) {
      currentUser = LoginHelper.getLoginUser();
      DataPermissionHelper.setVariable("user", currentUser);
    }
    // 如果是超级管理员或租户管理员，则不过滤数据
    if (LoginHelper.isSuperAdmin() || LoginHelper.isTenantAdmin()) {
      return;
    }
    String dataFilterSql = buildDataFilter(dataColumns, isSelect);
    if (StringUtils.isBlank(dataFilterSql)) {
      return;
    }
    if (queryWrapper == null) {
      queryWrapper = new QueryWrapper();
    }
    queryWrapper.and(dataFilterSql);
  }

  public String getSQL(DataPermission dataPermission, boolean isSelect) {
    return buildDataFilter(dataPermission.getValue(), isSelect);
  }


    /**
     * 构建数据过滤条件的 SQL 语句
     *
     * @param dataPermission 数据权限注解
     * @param isSelect       标志当前操作是否为查询操作，查询操作和更新或删除操作在处理过滤条件时会有不同的处理方式
     * @return 构建的数据过滤条件的 SQL 语句
     * @throws ServiceException 如果角色的数据范围异常或者 key 与 value 的长度不匹配，则抛出 ServiceException 异常
     */
  private String buildDataFilter(DataColumn[] dataColumns, boolean isSelect) {
    // 更新或删除需满足所有条件
    String joinStr = isSelect ? " OR " : " AND ";
    log.info("join  str : {}", joinStr);
    LoginUser user = DataPermissionHelper.getVariable("user");
    StandardEvaluationContext context = new StandardEvaluationContext();
    context.setBeanResolver(beanResolver);
    DataPermissionHelper.getContext().forEach(context::setVariable);
    Set<String> conditions = new HashSet<>();
    for (RoleDTO role : user.getRoles()) {
      user.setRoleId(role.getRoleId());
      // 获取角色权限泛型
      DataScopeType type = DataScopeType.findCode(role.getDataScope());
      if (ObjectUtil.isNull(type)) {
        throw new ServiceException("角色数据范围异常 => " + role.getDataScope());
      }
      // 全部数据权限直接返回
      if (type == DataScopeType.ALL) {
        return "";
      }
      boolean isSuccess = false;
      for (DataColumn dataColumn : dataColumns) {
        if (dataColumn.getKey().length != dataColumn.getValue().length) {
          throw new ServiceException("角色数据范围异常 => getKey与getValue长度不匹配");
        }
        // 不包含 getKey 变量 则不处理
        if (!StringUtils.containsAny(
            type.getSqlTemplate(),
            Arrays.stream(dataColumn.getKey())
                .map(getKey -> "#" + getKey)
                .toArray(String[]::new))) {
          continue;
        }
        // 设置注解变量 getKey 为表达式变量 getValue 为变量值
        for (int i = 0; i < dataColumn.getKey().length; i++) {
          context.setVariable(dataColumn.getKey()[i], dataColumn.getValue()[i]);
        }

        // 解析sql模板并填充
        String sql =
            parser
                .parseExpression(type.getSqlTemplate(), parserContext)
                .getValue(context, String.class);
          log.info("join  sql 1 : {}", sql);
        conditions.add(joinStr + sql);
          log.info("join  sql 2 : {}",joinStr + sql);

        isSuccess = true;
      }
      // 未处理成功则填充兜底方案
      if (!isSuccess && StringUtils.isNotBlank(type.getElseSql())) {
          log.info("join  sql 3 ");
        conditions.add(joinStr + type.getElseSql());
      }
    }

    if (CollUtil.isNotEmpty(conditions)) {
        log.info("join  sql 4 ");
      String sql = StreamUtils.join(conditions, Function.identity(), "");
      return sql.substring(joinStr.length());
    }
      log.info("join  sql 5 ");
    return "";
  }
}
