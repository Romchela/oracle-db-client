package ru.nsu.romchela.db;

import ru.nsu.romchela.db.changes.Change;
import ru.nsu.romchela.db.changes.DeleteTableChange;
import ru.nsu.romchela.db.changes.RenameTableChange;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Controller {
    private OracleDB db;
    private TableDB table;
    private String name;
    private List<String> deletedTables;
    private Map<String, String> tableNameRename;

    public Controller() {
        this.db = new OracleDB();
        deletedTables = new ArrayList<>();
        tableNameRename = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void newRecord(List<String> data) {
        table.add(data);
    }

    public void loadTable(String tableName) throws Exception {
        table = db.getTable(tableName);
        name = tableName;
    }

    public void updateHeader(int pos, String name) {
        table.updateHeader(pos, name);
    }

    public TableDB getTable() {
        return table;
    }

    public void updateCell(int x, int y, String value) {
        table.newValue(x, y, value);
    }

    public void synchronize() throws SQLException {
        List<Change> changes = new ArrayList<>();
        if (table != null) {
            changes = table.getChanges();
        }
        for (String name : deletedTables) {
            changes.add(new DeleteTableChange(name));
        }
        String newName = null;
        for (Map.Entry<String, String> entry : tableNameRename.entrySet()) {
            if (name != null && name.equals(entry.getKey())) {
                newName = entry.getValue();
            }
            changes.add(new RenameTableChange(entry.getKey(), entry.getValue()));
        }

        List<String> queries = new ArrayList<>();
        for (Change change : changes) {
            queries.add(change.getQuery());
        }

        db.doTransaction(queries);
        deletedTables.clear();
        tableNameRename.clear();
        if (newName != null) {
            name = newName;
        }
        if (name != null) {
            table = db.getTable(name);
        }
    }

    public void deleteRecord(int pos) {
        table.deleteRecord(pos);
    }

    public void reset() {
        if (table != null) {
            table.reset();
        }
        deletedTables.clear();
        tableNameRename.clear();
    }

    public void connect(String ip, String port, String tablespace, String login, String password) throws Exception {
       db.connect(ip, port, tablespace, login, password);
    }

    public void query(String query) throws SQLException {
        db.query(query);
    }
    public void disconnect() throws SQLException {
        db.disconnect();
    }

    public List<String> getAllTables() throws SQLException {
        return db.getAllTables();
    }

    public void deleteTable(String name) {
        deletedTables.add(name);
    }

    public void renameTable(String before, String after) {
        tableNameRename.put(before, after);
    }

}
