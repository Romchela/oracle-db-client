package ru.nsu.romchela.db.changes;

import java.util.List;

public class DeleteRecordChange extends Change {
    public DeleteRecordChange(String tableName, List<String> headers, List<String> before, List<Integer> types) {
        this.tableName = tableName;
        this.headers = headers;
        this.before = before;
        this.types = types;
    }

    @Override
    public String getQuery() {
        StringBuilder q = new StringBuilder();
        q.append("DELETE FROM ").append(tableName).append(" WHERE ");
        for (int i = 0; i < headers.size(); i++) {
            q.append(headers.get(i)).append(SqlUtils.getStringValueWHERE(before.get(i), types.get(i)));
            if (i != headers.size() - 1) {
                q.append(" and ");
            }
        }

        return q.toString();
    }
}
