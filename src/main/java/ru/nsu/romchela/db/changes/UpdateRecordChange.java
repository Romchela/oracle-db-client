package ru.nsu.romchela.db.changes;

import java.util.List;

public class UpdateRecordChange extends Change {
    public UpdateRecordChange(String tableName, List<String> headers, List<String> before, List<String> after, List<Integer> types) {
        this.tableName = tableName;
        this.headers = headers;
        this.before = before;
        this.after = after;
        this.types = types;
    }

    @Override
    @SuppressWarnings("Duplicates")
    public String getQuery() {
        StringBuilder q = new StringBuilder();
        q.append("UPDATE ").append(tableName).append(" SET ");
        for (int i = 0; i < headers.size(); i++) {
            q.append(headers.get(i)).append(SqlUtils.getStringValueSET(after.get(i), types.get(i)));
            if (i != headers.size() - 1) {
                q.append(",");
            }
            q.append(" ");
        }
        q.append("WHERE ");
        for (int i = 0; i < headers.size(); i++) {
            q.append(headers.get(i)).append(SqlUtils.getStringValueWHERE(before.get(i), types.get(i)));
            if (i != headers.size() - 1) {
                q.append(" and ");
            }
        }

        return q.toString();
    }
}
