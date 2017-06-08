package ru.nsu.romchela.db.view;

import ru.nsu.romchela.db.Controller;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SideView extends JPanel {
    private Controller controller;
    private List<JButton> tableButtons;
    private ContentView view;
    private List<JButton> deleteButtons;
    private List<JButton> renameButtons;
    private MainWindow mainWindow;

    public SideView(Controller controller, ContentView view, MainWindow mainWindow) {
        this.controller = controller;
        this.view = view;
        this.mainWindow = mainWindow;
        tableButtons = new ArrayList<>();
        deleteButtons = new ArrayList<>();
        renameButtons = new ArrayList<>();
        setLayout(new GridBagLayout());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    public void deleteButton(int pos) {
        JButton button = tableButtons.get(pos);
        button.getParent().remove(button);
        tableButtons.remove(pos);
        JButton deleteButton = deleteButtons.get(pos);
        deleteButton.getParent().remove(deleteButton);
        deleteButtons.remove(pos);
        JButton renameButton = renameButtons.get(pos);
        renameButton.getParent().remove(renameButton);
        renameButtons.remove(pos);
        updateUI();
        repaint();
    }

    public void showTables() {
        try {
            List<String> tables = controller.getAllTables();
            tableButtons = new ArrayList<>();
            int position = 0;
            for (String name : tables) {
                JButton button = new JButton(name);
                button.addActionListener(l -> {
                    view.loadTable(button.getText());
                    mainWindow.activateButtons();
                });
                tableButtons.add(button);

                GridBagConstraints c = new GridBagConstraints();
                c.anchor = GridBagConstraints.NORTH;
                c.gridx = 0;
                c.gridy = position;
                c.fill = GridBagConstraints.BOTH;

                GridBagConstraints c1 = new GridBagConstraints();
                c1.anchor = GridBagConstraints.NORTH;
                c1.gridx = 2;
                c1.gridy = position;
                BufferedImage buttonIcon = ImageIO.read(new File(getClass().getResource("/delete.png").getPath()));
                JButton deleteButton = new JButton(new ImageIcon(buttonIcon));
                final int finalPos = position;
                final String finalName = name;
                deleteButton.addActionListener(l -> {
                    deleteButton(finalPos);
                    controller.deleteTable(finalName);
                    view.hideTable(finalName);
                });
                deleteButtons.add(deleteButton);


                GridBagConstraints c2 = new GridBagConstraints();
                c2.anchor = GridBagConstraints.NORTH;
                c2.gridx = 1;
                c2.gridy = position;
                BufferedImage buttonIconRename = ImageIO.read(new File(getClass().getResource("/edit.png").getPath()));
                RenameButton renameButton = new RenameButton(new ImageIcon(buttonIconRename));
                renameButton.setTableName(name);
                renameButton.addActionListener(l -> {
                    RenameTableFrame frame = new RenameTableFrame();
                    int choice = JOptionPane.showConfirmDialog(getParent().getParent().getParent(), frame, "Rename table", JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.PLAIN_MESSAGE);
                    if (choice == JOptionPane.OK_OPTION) {
                        String newName = frame.getNewName();
                        tableButtons.get(finalPos).setText(newName);
                        controller.renameTable(finalName, newName);
                        repaint();
                        updateUI();
                    }
                });
                renameButtons.add(renameButton);

                add(deleteButton, c1);
                add(renameButton, c2);
                add(button, c);
                position++;

            }
        } catch (Exception e) {
            JOptionPane.showConfirmDialog(this, e.getMessage(), "Error", JOptionPane.DEFAULT_OPTION,
                    JOptionPane.ERROR_MESSAGE);
        }
        repaint();
        updateUI();
    }

    public void hideTables() {
        for (JButton button : tableButtons) {
            button.getParent().remove(button);
        }
        for (JButton button : deleteButtons) {
            button.getParent().remove(button);
        }
        for (JButton button : renameButtons) {
            button.getParent().remove(button);
        }
        renameButtons.clear();
        tableButtons.clear();
        deleteButtons.clear();
        repaint();
        updateUI();
    }

    public void redraw() {
        hideTables();
        showTables();
    }

}
