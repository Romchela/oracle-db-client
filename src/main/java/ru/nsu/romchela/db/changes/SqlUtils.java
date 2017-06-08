package ru.nsu.romchela.db.changes;

import java.sql.Types;

public class SqlUtils {
    public static String getStringValueWHERE(String value, int type) {
        if (value.equals("")) {
            return " is NULL";
        } else if (type == Types.VARCHAR) {
            return "='" + value + "'";
        } else {
            return "=" + value;
        }
    }

    public static String getStringValueSET(String value, int type) {
        if (value.equals("")) {
            return "= NULL";
        } else if (type == Types.VARCHAR) {
            return "='" + value + "'";
        } else {
            return "=" + value;
        }
    }

    public static String getStringValue(String value, int type) {
        if (value.equals("")) {
            return "NULL";
        } else if (type == Types.VARCHAR) {
            return "'" + value + "'";
        } else {
            return value;
        }
    }
}
