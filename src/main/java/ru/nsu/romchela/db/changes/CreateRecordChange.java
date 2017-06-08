package ru.nsu.romchela.db.changes;

import java.util.List;

public class CreateRecordChange extends Change {
    public CreateRecordChange(String tableName, List<String> headers, List<String> after, List<Integer> types) {
        this.tableName = tableName;
        this.headers = headers;
        this.types = types;
        this.after = after;
    }

    @Override
    public String getQuery() {
        StringBuilder q = new StringBuilder();
        q.append("INSERT INTO ").append(tableName).append(" (");
        for (int i = 0; i < headers.size(); i++) {
            q.append("\"").append(headers.get(i)).append("\"");
            if (i != headers.size() - 1) {
                q.append(",");
            }
        }
        q.append(") VALUES(");
        for (int i = 0; i < headers.size(); i++) {
            q.append(SqlUtils.getStringValue(after.get(i), types.get(i)));
            if (i != headers.size() - 1) {
                q.append(",");
            }
        }
        q.append(")");

        return q.toString();
    }
}
