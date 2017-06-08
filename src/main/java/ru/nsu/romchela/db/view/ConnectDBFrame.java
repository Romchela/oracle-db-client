package ru.nsu.romchela.db.view;

import javax.swing.*;
import java.awt.*;

public class ConnectDBFrame extends JPanel {
    private JTextField ip;
    private JTextField port;
    private JTextField login;
    private JTextField password;
    private JTextField tablespace;

    public String getTablespace() {
        return tablespace.getText();
    }

    public String getPassword() {
        return password.getText();
    }

    public String getLogin() {
        return login.getText();
    }

    public String getPort() {
        return port.getText();
    }

    public String getIp() {
        return ip.getText();
    }

    public JTextField addTextField(String label, int position) {
        JLabel text = new JLabel(label);
        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(150, 25));

        GridBagConstraints c1 = new GridBagConstraints();
        c1.anchor = GridBagConstraints.WEST;
        c1.gridx = 0;
        c1.gridy = position;
        c1.insets = new Insets(1, 0, 0, 4);
        add(text, c1);

        GridBagConstraints c2 = new GridBagConstraints();
        c2.gridx = 1;
        c2.gridy = position;
        c2.insets = new Insets(1, 0, 0, 0);
        add(textField, c2);
        return textField;
    }

    public ConnectDBFrame() {
        super();
        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);

        ip = addTextField("IP address", 0);
        port = addTextField("Port", 1);
        tablespace = addTextField("Tablespace", 2);
        login = addTextField("Login", 3);
        password = addTextField("Password", 4);

        ip.setText("localhost");
        port.setText("1521");
        tablespace.setText("orcl12c");
        login.setText("system");
        password.setText("oracle");
    }


}
