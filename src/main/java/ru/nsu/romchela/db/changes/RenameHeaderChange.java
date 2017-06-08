package ru.nsu.romchela.db.changes;

import java.util.ArrayList;

public class RenameHeaderChange extends Change {
    public RenameHeaderChange(String name, String before, String after) {
        this.tableName = name;
        this.before = new ArrayList<>();
        this.before.add(before);
        this.after = new ArrayList<>();
        this.after.add(after);
    }

    @Override
    public String getQuery() {
        StringBuilder q = new StringBuilder();
        q.append("ALTER TABLE ").append(tableName).append(" RENAME COLUMN ").append(before.get(0)).append(" TO ").append(after.get(0));
        return q.toString();
    }
}
