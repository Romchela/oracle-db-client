package ru.nsu.romchela.db.view;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class RenameTableFrame extends JPanel {
    private JTextField field;

    public RenameTableFrame() {
        add(new JLabel("New name"));
        field = new JTextField("", 15);
        add(field);
    }

    public String getNewName() {
        return field.getText();
    }
}
