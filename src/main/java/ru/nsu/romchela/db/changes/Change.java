package ru.nsu.romchela.db.changes;

import java.util.List;

public abstract class Change {
    protected List<String> headers;
    protected List<String> before, after;
    protected String tableName;
    protected List<Integer> types;

    public abstract String getQuery();
}
