package ru.nsu.romchela.db.view;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class RenameButton extends JButton {
    private String tableName;

    public RenameButton(ImageIcon icon) {
        super(icon);
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
