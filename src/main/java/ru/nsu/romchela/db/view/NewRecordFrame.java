package ru.nsu.romchela.db.view;

import ru.nsu.romchela.db.Controller;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

public class NewRecordFrame extends JPanel {
    private Controller controller;
    private List<JTextField> fields;

    public NewRecordFrame(Controller controller) {
        this.controller = controller;
        setLayout(new GridBagLayout());
        List<String> headers = controller.getTable().getHeaders();
        fields = new ArrayList<>();
        for (int i = 0; i < headers.size(); i++) {
            GridBagConstraints c1 = new GridBagConstraints();
            c1.anchor = GridBagConstraints.WEST;
            c1.gridx = 0;
            c1.gridy = i;
            //c1.insets = new Insets(1, 0, 0, 4);
            c1.fill = GridBagConstraints.BOTH;
            add(new JLabel(headers.get(i)), c1);

            GridBagConstraints c2 = new GridBagConstraints();
            c2.anchor = GridBagConstraints.WEST;
            c2.gridx = 1;
            c2.gridy = i;
            c2.weightx = 100;
            c2.weighty = 100;
            c2.fill = GridBagConstraints.BOTH;
            //c2.insets = new Insets(1, 0, 0, 4);

            JTextField text = new JTextField();
            fields.add(text);
            add(text, c2);
        }
    }

    public List<String> getValues() {
        List<String> result = new ArrayList<>();
        for (JTextField field : fields) {
            result.add(field.getText());
        }
        return result;
    }


}
