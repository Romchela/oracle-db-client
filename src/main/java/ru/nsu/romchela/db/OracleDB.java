package ru.nsu.romchela.db;

import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OracleDB {
    private Connection connection;

    public boolean isConnected() {
        try {
            return connection.isValid(20);
        } catch (SQLException e) {
            return false;
        }
    }

    public void connect(String ip, String port, String tablespace, String login, String password) throws Exception {
        String url = "jdbc:oracle:thin:@//" + ip + ":" + port +"/" + tablespace;
        Class.forName("oracle.jdbc.OracleDriver");
        connection = DriverManager.getConnection(url, login, password);
    }

    public TableDB query(String query) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(query);
        return getTable(result, "Query");
    }

    public void doTransaction(List<String> queries) throws SQLException {
        try {
            connection.setAutoCommit(false);
            for (String query : queries) {
                Statement statement = connection.createStatement();
                statement.executeQuery(query);
            }
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            connection.setAutoCommit(true);
            throw e;
        }
        connection.setAutoCommit(true);
    }

    public TableDB getTable(String tableName) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM " + tableName);
        return getTable(result, tableName);
    }

    private TableDB getTable(ResultSet result, String tableName) throws SQLException {
        ResultSetMetaData data = result.getMetaData();
        int columnsNumber = data.getColumnCount();

        List<String> headers = new ArrayList<>();
        for (int i = 1; i <= columnsNumber; i++) {
            headers.add(data.getColumnName(i));
        }

        List<Integer> types = new ArrayList<>();
        for (int i = 1; i <= columnsNumber; i++) {
            types.add(data.getColumnType(i));
        }

        List<List<String>> table = new ArrayList<>();
        while (result.next()) {
            List<String> str = new ArrayList<>();
            for (int i = 1; i <= columnsNumber; i++) {
                String s = result.getString(i);
                if (s == null) {
                    str.add("");
                } else {
                    str.add(result.getString(i));
                }
            }
            table.add(str);
        }
        return new TableDB(tableName, table, headers, types);
    }

    public List<String> getAllTables() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery("SELECT table_name FROM user_tables");
        List<String> tables = new ArrayList<>();
        while (result.next()) {
            tables.add(result.getString("table_name"));
        }
        return tables;
    }

    public void disconnect() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }


}
