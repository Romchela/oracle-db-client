package ru.nsu.romchela.db.view;

import javax.swing.*;
import java.awt.*;

public class QueryFrame extends JPanel {
    private JTextField query;

    public String getQuery() {
        return query.getText();
    }

    public QueryFrame() {
        super();
        query = new JTextField();
        query.setHorizontalAlignment(JTextField.CENTER);
        query.setPreferredSize(new Dimension(300, 200));
        query.setText("SELECT * FROM STUDENTS");
        add(query);
    }
}
