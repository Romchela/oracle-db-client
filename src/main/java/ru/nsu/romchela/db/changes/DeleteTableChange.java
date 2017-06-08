package ru.nsu.romchela.db.changes;


public class DeleteTableChange extends Change {
    public DeleteTableChange(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public String getQuery() {
        StringBuilder q = new StringBuilder();
        q.append("DROP TABLE ").append(tableName);
        return q.toString();
    }
}
