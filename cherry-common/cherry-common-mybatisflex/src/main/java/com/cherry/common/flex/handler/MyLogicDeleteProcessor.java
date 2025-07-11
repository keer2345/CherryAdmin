package com.cherry.common.flex.handler;

import com.cherry.common.satoken.utils.LoginHelper;
import com.mybatisflex.core.dialect.IDialect;
import com.mybatisflex.core.logicdelete.impl.DefaultLogicDeleteProcessor;
import com.mybatisflex.core.table.TableInfo;

import java.util.Arrays;
import java.util.List;

import static com.mybatisflex.core.constant.SqlConsts.EQUALS;

/**
 * MyLogicDeleteProcessor
 *
 * PostgreSQL
 *
 * @author keer2345
 * @date 2025-06-24
 */
public class MyLogicDeleteProcessor extends DefaultLogicDeleteProcessor {
    //
    // https://github.com/feiyuchuixue/sz-boot-parent/blob/e2865a9/sz-common/sz-common-db-mysql/src/main/java/com/sz/mysql/EntityLogicDeleteListener.java
    private static final String FIELD_DELETE_TIME = "delete_time";

    private static final String FIELD_DELETE_ID = "delete_id";

    @Override
    public String buildLogicDeletedSet(String logicColumn, TableInfo tableInfo, IDialect dialect) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder
            .append(dialect.wrap(logicColumn))
            .append(EQUALS)
            .append(prepareValue(getLogicDeletedValue()));

        List<String> columns = Arrays.asList(tableInfo.getAllColumns());

        if (columns.contains(FIELD_DELETE_TIME)) {
            sqlBuilder
                .append(", ")
                .append(dialect.wrap(FIELD_DELETE_TIME))
                .append(EQUALS)
                .append("now()");
        }

        //        boolean isLogin = StpUtil.isLogin();
        boolean isLogin = true;
        if (isLogin && columns.contains(FIELD_DELETE_ID)) {
            sqlBuilder
                .append(", ")
                .append(dialect.wrap(FIELD_DELETE_ID))
                .append(EQUALS)
                .append(prepareValue(LoginHelper.getUserId()));
        }else {
            sqlBuilder
                .append(", ")
                .append(dialect.wrap(FIELD_DELETE_ID))
                .append(EQUALS)
                .append(prepareValue(""));
        }

        return sqlBuilder.toString();
    }

    private static Object prepareValue(Object value) {
        return (!(value instanceof Number) && !(value instanceof Boolean)) ? "'" + value + "'" : value;
    }
}
