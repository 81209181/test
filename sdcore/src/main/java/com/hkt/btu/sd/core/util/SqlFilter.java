package com.hkt.btu.sd.core.util;

import org.apache.commons.lang3.StringUtils;

public class SqlFilter {

    private static final String SQL_INJ_STR = "and|exec|insert|delete|update|\n" +
            "\n" +
            "|chr|mid|master|truncate|char|declare|;|-|,";

    public static String filter(String sql) {
        if (StringUtils.isNotBlank(sql)) {
            sql = sql.toLowerCase()
                     .replaceAll(SQL_INJ_STR, "");
        }
        return sql;
    }
}
