package ru.nsu.romchela.db.view;

import ru.nsu.romchela.db.Controller;

import javax.swing.*;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class MainWindow extends MainFrame {

    private Controller controller;

    private ContentView view;
    private SideView sideView;
    private List<JButton> afterConnectionButtons;
    private List<JButton> afterTableOpenButtons;

    public void activateButtons() {
        for (JButton button : afterTableOpenButtons) {
            button.setEnabled(true);
        }
    }

    public MainWindow() {
        super(SettingsConstants.WINDOW_WIDTH, SettingsConstants.WINDOW_HEIGHT, SettingsConstants.WINDOW_HEADER);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

            }
        });

        afterConnectionButtons = new ArrayList<>();
        afterTableOpenButtons = new ArrayList<>();

        try {
            addSubMenu("Client", KeyEvent.VK_F);
            addMenuItem("Client/Connect", "New", KeyEvent.VK_L, "connect.png", "onConnect");
            addMenuItem("Client/Disconnect", "Disconnect", KeyEvent.VK_S, "disconnect.png", "onDisconnect");
            addMenuItem("Client/Exit", "Exit application", KeyEvent.VK_E, "exit.png", "onExit");
            addSubMenu("Table", KeyEvent.VK_F);
            addMenuItem("Table/Add record", "Add record", KeyEvent.VK_L, "new_record.png", "onNewRecord");
            addMenuItem("Table/Custom query", "Custom query", KeyEvent.VK_L, "query.png", "onQuery");
            addMenuItem("Table/Reset", "Reset", KeyEvent.VK_S, "undo.png", "onReset");
            addMenuItem("Table/Sync", "Sync", KeyEvent.VK_S, "sync.png", "onSynchronize");
            addSubMenu("Help", KeyEvent.VK_H);
            addMenuItem("Help/About...", "About author...", KeyEvent.VK_A, "About.png", "onAbout");


            addToolBarButton("Client/Connect");
            afterConnectionButtons.add(addToolBarButton("Client/Disconnect"));
            addToolBarButton("Client/Exit");
            addToolBarSeparator();
            afterTableOpenButtons.add(addToolBarButton("Table/Add record"));
            afterTableOpenButtons.add(addToolBarButton("Table/Custom query"));
            afterConnectionButtons.add(addToolBarButton("Table/Sync"));
            afterConnectionButtons.add(addToolBarButton("Table/Reset"));
            addToolBarSeparator();
            addToolBarButton("Help/About...");


            controller = new Controller();
            view = new ContentView(controller);
            view.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
            sideView = new SideView(controller, view, this);
            sideView.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

            JPanel container = new JPanel();
            container.setLayout(new GridBagLayout());

            GridBagConstraints c1 = new GridBagConstraints();
            c1.gridx = 0;
            c1.gridy = 0;
            c1.gridwidth = 1;
            c1.gridheight = 1;
            c1.weighty = 100;
            c1.weightx = 70;
            c1.fill = GridBagConstraints.BOTH;
            c1.anchor = GridBagConstraints.NORTH;

            GridBagConstraints c2 = new GridBagConstraints();
            c2.anchor = GridBagConstraints.NORTH;
            c2.gridx = 1;
            c2.gridy = 0;
            c2.ipadx = 350;
            c1.weighty = 100;
            c2.fill = GridBagConstraints.BOTH;


            container.add(new JScrollPane(view), c1);
            container.add(new JScrollPane(sideView), c2);
            add(container);

            for (JButton button : afterTableOpenButtons) {
                button.setEnabled(false);
            }
            for (JButton button : afterConnectionButtons) {
                button.setEnabled(false);
            }
        } catch (NoSuchMethodException e) {
            Logger.getGlobal().warning("NoSuchMethodException MainWindow");
        }
    }

    public void onNewRecord() {
        NewRecordFrame frame = new NewRecordFrame(controller);
        int choice = JOptionPane.showConfirmDialog(this, frame, "New record", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (choice == JOptionPane.OK_OPTION) {
            try {
                controller.newRecord(frame.getValues());
            } catch (Exception e) {
                JOptionPane.showConfirmDialog(this, e.getMessage(), "Error", JOptionPane.DEFAULT_OPTION,
                        JOptionPane.ERROR_MESSAGE);
                onNewRecord();
            }
        }
    }

    public void onConnect() {
        ConnectDBFrame frame = new ConnectDBFrame();
        int choice = JOptionPane.showConfirmDialog(this, frame, "New database connection", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (choice == JOptionPane.OK_OPTION) {
            onDisconnect();
            try {
                controller.connect(frame.getIp(), frame.getPort(), frame.getTablespace(), frame.getLogin(), frame.getPassword());
            } catch (Exception e) {
                JOptionPane.showConfirmDialog(this, e.getMessage(), "Error", JOptionPane.DEFAULT_OPTION,
                        JOptionPane.ERROR_MESSAGE);
                onConnect();
                return;
            }
            JOptionPane.showMessageDialog(this, "Connected!", "Message", JOptionPane.INFORMATION_MESSAGE, null);
            sideView.showTables();
            for (JButton button : afterConnectionButtons) {
                button.setEnabled(true);
            }
            repaint();
        }
    }

    public void onSynchronize() {
        try {
            controller.synchronize();
            view.redraw();
            sideView.redraw();
            JOptionPane.showMessageDialog(this, "Synchronized!", "Message", JOptionPane.INFORMATION_MESSAGE, null);
        } catch (Exception e) {
            JOptionPane.showConfirmDialog(this, e.getMessage(), "Error", JOptionPane.DEFAULT_OPTION,
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void onDisconnect() {
        try {
            controller.disconnect();
            sideView.hideTables();
            view.hideTable();
            for (JButton button : afterTableOpenButtons) {
                button.setEnabled(false);
            }
            for (JButton button : afterConnectionButtons) {
                button.setEnabled(false);
            }
        } catch (Exception e) {
            JOptionPane.showConfirmDialog(this, e.getMessage(), "Error", JOptionPane.DEFAULT_OPTION,
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void onReset() {
        controller.reset();
        view.redraw();
        sideView.redraw();
    }

    public void onExit() {
        System.exit(0);
    }

    public void onQuery() {
        QueryFrame frame = new QueryFrame();
        int choice = JOptionPane.showConfirmDialog(this, frame, "Custom query", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (choice == JOptionPane.OK_OPTION) {
            try {
                controller.query(frame.getQuery());
            } catch (Exception e) {
                JOptionPane.showConfirmDialog(this, e.getMessage(), "Error", JOptionPane.DEFAULT_OPTION,
                        JOptionPane.ERROR_MESSAGE);
                onQuery();
            }
        }
    }

    public void onAbout() {
        ImageIcon icon = new ImageIcon(getClass().getResource("/Romchela.jpg"));
        JOptionPane.showMessageDialog(this,
                "Oracle DB client, version 0.1\nAuthor: Roman Vishnevsky\nUniversity: Novosibirsk SU\n" +
                        "Company: Xored Software Inc.\n", "About application",
                            JOptionPane.INFORMATION_MESSAGE, icon);
    }

    public static void main(String[] args) {
        MainWindow mainFrame = new MainWindow();
        mainFrame.setVisible(true);
    }
}