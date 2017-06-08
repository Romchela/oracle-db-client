package ru.nsu.romchela.db.view;

import ru.nsu.romchela.db.Controller;
import ru.nsu.romchela.db.TableDB;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ContentView extends JPanel {
    private Controller controller;

    private List<JPanel> cells;
    private List<JTextField> headers;
    private List<JPanel> deleteIcons;
    private List<JPanel> emptyPanels;

    public ContentView(Controller controller) {
        this.controller = controller;
        cells = new ArrayList<>();
        headers = new ArrayList<>();
        deleteIcons = new ArrayList<>();
        emptyPanels = new ArrayList<>();
    }

    public void loadTable(String tableName) {
        try {
            controller.loadTable(tableName);
            showTable();
        } catch (Exception e) {
            JOptionPane.showConfirmDialog(this, e.getMessage(), "Error", JOptionPane.DEFAULT_OPTION,
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void redraw() {
        hideTable();
        showTable();
    }

    public void showTable() {
        hideTable();
        if (controller.getName() == null) {
            return;
        }
        try {
            TableDB table = controller.getTable();
            setLayout(new GridBagLayout());
            for (int i = 0; i < table.getWidth(); i++) {
                GridBagConstraints c = new GridBagConstraints();
                c.insets = new Insets(2, 7, 2, 2);
                c.gridx = i;
                c.gridy = 0;
                c.weightx = 100;
                c.weighty = 0;
                c.fill = GridBagConstraints.BOTH;
                JTextField label = new JTextField(table.getHeader(i));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                final int pos = i;
                label.getDocument().addDocumentListener(new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        controller.updateHeader(pos, label.getText());
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        controller.updateHeader(pos, label.getText());
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        controller.updateHeader(pos, label.getText());
                    }
                });
                headers.add(label);
                add(label, c);
            }

            int curY = 1;
            for (int i = 0; i < table.getHeight(); i++) {
                if (table.isDeleted(i)) {
                    continue;
                }
                for (int j = 0; j < table.getWidth(); j++) {
                    GridBagConstraints c = new GridBagConstraints();
                    c.gridx = j;
                    c.gridy = curY;
                    c.weightx = 100;
                    c.fill = GridBagConstraints.BOTH;

                    JPanel panel = new JPanel();
                    panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                    panel.setLayout(new GridBagLayout());

                    GridBagConstraints c1 = new GridBagConstraints();
                    c1.weightx = 100;
                    c1.weighty = 100;
                    c1.fill = GridBagConstraints.BOTH;
                    JTextField textField = new JTextField(table.getCellData(i, j));
                    final int x = i;
                    final int y = j;
                    textField.getDocument().addDocumentListener(new DocumentListener() {
                        @Override
                        public void insertUpdate(DocumentEvent e) {
                            controller.updateCell(x, y, textField.getText());
                        }

                        @Override
                        public void removeUpdate(DocumentEvent e) {
                            controller.updateCell(x, y, textField.getText());
                        }

                        @Override
                        public void changedUpdate(DocumentEvent e) {
                            controller.updateCell(x, y, textField.getText());
                        }
                    });
                    panel.add(textField, c1);

                    cells.add(panel);
                    add(panel, c);

                }

                GridBagConstraints c = new GridBagConstraints();
                c.gridx = table.getWidth();
                c.gridy = curY;

                JPanel panel = new JPanel();
                BufferedImage buttonIcon = ImageIO.read(new File(getClass().getResource("/delete.png").getPath()));
                JButton button = new JButton(new ImageIcon(buttonIcon));
                final int num = i;
                button.addActionListener(l -> {
                    controller.deleteRecord(num);
                    redraw();
                });
                panel.setMaximumSize(new Dimension(1, 1));
                panel.add(button);

                deleteIcons.add(panel);

                add(panel, c);
                curY++;
            }

            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = curY;
            c.weightx = 100;
            c.weighty = 100;
            c.fill = GridBagConstraints.BOTH;
            JPanel emptyPanel = new JPanel();
            emptyPanels.add(emptyPanel);
            add(emptyPanel, c);
            repaint();
            updateUI();
        } catch (Exception e) {
            JOptionPane.showConfirmDialog(this, e.getMessage(), "Error", JOptionPane.DEFAULT_OPTION,
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void hideTable() {
        for (JPanel cell : cells) {
            cell.getParent().remove(cell);
        }
        for (JTextField header : headers) {
            header.getParent().remove(header);
        }
        for (JPanel icon : deleteIcons) {
            icon.getParent().remove(icon);
        }
        for (JPanel panel : emptyPanels) {
            panel.getParent().remove(panel);
        }
        headers.clear();
        cells.clear();
        deleteIcons.clear();
        emptyPanels.clear();
        repaint();
        updateUI();
    }

    public void hideTable(String name) {
        if (controller.getName() != null && controller.getName().equals(name)) {
            hideTable();
        }
    }
}
