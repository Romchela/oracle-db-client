package ru.nsu.romchela.db;

import ru.nsu.romchela.db.changes.Change;
import ru.nsu.romchela.db.changes.CreateRecordChange;
import ru.nsu.romchela.db.changes.DeleteRecordChange;
import ru.nsu.romchela.db.changes.RenameHeaderChange;
import ru.nsu.romchela.db.changes.UpdateRecordChange;

import java.util.ArrayList;
import java.util.List;

public class TableDB {
    private int width, height;
    private List<String> headers;
    private List<String> currentHeaders;
    private List<List<String>> table;
    private List<List<String>> currentTable;
    private String name;
    private List<Integer> types;
    private List<Boolean> deleted;
    private List<List<String>> toAdd;

    public TableDB(String name, List<List<String>> table, List<String> headers, List<Integer> types) {
        toAdd = new ArrayList<>();
        deleted = new ArrayList<>();
        for (int i = 0; i < table.size(); i++) {
            deleted.add(false);
        }
        this.table = table;
        this.headers = headers;
        reset();
        this.name = name;
        this.types = types;
        setHeight(table.size()) ;
        setWidth(headers.size());

    }

    public void updateHeader(int pos, String value) {
        currentHeaders.set(pos, value);
    }

    public List<String> getHeaders() {
        return headers;
    }

    @SuppressWarnings("Duplicates")
    public void reset() {
        currentTable = new ArrayList<>();
        for (int i = 0; i < table.size(); i++) {
            List<String> l = new ArrayList<>();
            for (int j = 0; j < table.get(i).size(); j++) {
                l.add(table.get(i).get(j));
            }
            currentTable.add(l);
        }
        for (int i = 0; i < deleted.size(); i++) {
            deleted.set(i, false);
        }
        toAdd.clear();
        currentHeaders = new ArrayList<>();
        for (int i = 0; i < headers.size(); i++) {
            currentHeaders.add(headers.get(i));
        }
    }

    public void deleteRecord(int pos) {
        deleted.set(pos, true);
    }

    public boolean isDeleted(int pos) {
        return deleted.get(pos);
    }

    public List<Change> getChanges() {
        List<Change> result = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            if (deleted.get(i)) {
                result.add(new DeleteRecordChange(name, headers, table.get(i), types));
            } else {
                for (int j = 0; j < width; j++) {
                    if (!table.get(i).get(j).equals(currentTable.get(i).get(j))) {
                        result.add(new UpdateRecordChange(name, headers, table.get(i), currentTable.get(i), types));
                        break;
                    }
                }
            }
        }
        for (int i = 0; i < toAdd.size(); i++) {
            result.add(new CreateRecordChange(name, headers, toAdd.get(i), types));
        }
        for (int i = 0; i < headers.size(); i++) {
            if (!headers.get(i).equals(currentHeaders.get(i))) {
                result.add(new RenameHeaderChange(name, headers.get(i), currentHeaders.get(i)));
            }
        }
        return result;
    }

    @SuppressWarnings("Duplicates")
    public void synchronize() {
        table = new ArrayList<>();
        for (int i = 0; i < currentTable.size(); i++) {
            List<String> l = new ArrayList<>();
            for (int j = 0; j < currentTable.get(i).size(); j++) {
                l.add(currentTable.get(i).get(j));
            }
            table.add(l);
        }
        headers = new ArrayList<>();
        for (int i = 0; i < headers.size(); i++) {
            headers.add(currentHeaders.get(i));
        }
    }

    public void newValue(int x, int y, String data) {
        currentTable.get(x).set(y, data);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getHeader(int pos) {
        return headers.get(pos);
    }

    public String getCellData(int x, int y) {
        return currentTable.get(x).get(y);
    }

    public void add(List<String> data) {
        toAdd.add(data);
    }
}
