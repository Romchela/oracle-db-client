package ru.nsu.romchela.db.changes;

import java.util.ArrayList;

public class RenameTableChange extends Change {
    public RenameTableChange(String before, String after) {
        this.before = new ArrayList<>();
        this.before.add(before);
        this.after = new ArrayList<>();
        this.after.add(after);
    }

    @Override
    public String getQuery() {
        StringBuilder q = new StringBuilder();
        q.append("ALTER TABLE ").append(before.get(0)).append(" RENAME TO ").append(after.get(0));
        return q.toString();
    }
}
