package simulator.views;

import org.json.JSONObject;
import simulator.control.Controller;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ForceLawDialog extends JDialog {
    private static final long serialVersionUID = 1L;
    private Toolkit screen = Toolkit.getDefaultToolkit();
    private Dimension screenDimension = screen.getScreenSize();
    private Controller controller;

    public ForceLawDialog(Frame parent, Controller controller) {
        super(parent, true);
        this.controller = controller;
        setTitle("Force Laws Selection");
        setSize(2*parent.getWidth()/3, 2*parent.getHeight()/3);
        setLocation(screenDimension.width/3, screenDimension.height/3);
        setResizable(true);
        init();
        setVisible(true);
    }

    private void init() {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        setContentPane(container);

        JLabel infoText = new JLabel("<html> Select a force law and provide values for the parameters in the <br>Value column</br> (default values are used for parameters with no values).</html>");
        infoText.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(infoText);
        container.add(Box.createRigidArea(new Dimension(0, 15)));

        ForceLawParametersTable forceLawParametersTable = new ForceLawParametersTable();
        JTable parametersTable = new JTable(forceLawParametersTable);
        parametersTable.setShowGrid(false);
        JScrollPane tableScroll = new JScrollPane(parametersTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        container.add(tableScroll);

        JPanel forceLawSelector = new JPanel();
        forceLawSelector.setLayout(new FlowLayout());
        JLabel forceLawLabel = new JLabel("Force Law: ", JLabel.CENTER);
        forceLawLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        forceLawSelector.add(forceLawLabel);
        forceLawSelector.add(new JSeparator());
        List<JSONObject> forceLawsInfoList = controller.getForceLawInfo();
        JComboBox<String> forceLawsJComboBox = new JComboBox<>();

        Map<Integer, JSONObject> forceLaws = new HashMap<>();

        for(JSONObject jsonObject : forceLawsInfoList) {
            String desc = jsonObject.getString("desc");
            forceLaws.put(forceLawsInfoList.indexOf(jsonObject), jsonObject);
            forceLawsJComboBox.addItem(desc);
        }

        forceLawsJComboBox.setVisible(true);

        forceLawsJComboBox.addActionListener(actionEvent -> {
            int selectedForceLaw = forceLawsJComboBox.getSelectedIndex();
            forceLawParametersTable.clear();
            String forceType = forceLaws.get(selectedForceLaw).getString("type");
            JSONObject data = forceLaws.get(selectedForceLaw).getJSONObject("data");
            switch (forceType) {
                case "nlug":
                    String G = (String) data.getString("G");
                    forceLawParametersTable.clear();
                    forceLawParametersTable.setValueAt("G", 0,0);
                    forceLawParametersTable.setValueAt(G, 0, 2);
                    forceLawParametersTable.fireTableDataChanged();
                    break;
                case "mtcp":
                    String cInfo = (String) data.getString("c");
                    String gInfo = (String) data.getString("g");
                    forceLawParametersTable.clear();
                    forceLawParametersTable.setValueAt("c", 0,0);
                    forceLawParametersTable.setValueAt(cInfo, 0,2);
                    forceLawParametersTable.setValueAt("g", 1, 0);
                    forceLawParametersTable.setValueAt(gInfo, 1, 2);
                    forceLawParametersTable.fireTableDataChanged();
                    break;
                case "nf":
                    forceLawParametersTable.clear();
                    forceLawParametersTable.fireTableDataChanged();
                    break;
                default:
                    break;
            }
        });
        forceLawSelector.add(forceLawsJComboBox);
        container.add(forceLawSelector);

        container.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel okCancel = new JPanel();
        okCancel.setLayout(new FlowLayout());
        okCancel.setAlignmentX(Component.CENTER_ALIGNMENT);
        okCancel.setVisible(true);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(actionEvent -> ForceLawDialog.this.setVisible(false));

        okCancel.add(cancelButton);
        okCancel.add(new JSeparator());

        JButton okButton = new JButton("OK");

        // TODO: Create properly a force law
        okButton.addActionListener(actionEvent -> {
            int selectedForceLaw = forceLawsJComboBox.getSelectedIndex();
            String forceType = forceLaws.get(selectedForceLaw).getString("type");
            JSONObject force = new JSONObject();
            force.put("type", forceType);
            JSONObject forceData = new JSONObject(forceLawParametersTable.getData());
            force.put("data", forceData);
            controller.setForceLaws(force);
            System.out.println(force.toString());
            ForceLawDialog.this.setVisible(false);
        });

        okCancel.add(okButton);
        container.add(okCancel);
    }

}
