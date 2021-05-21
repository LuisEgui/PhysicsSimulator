package simulator.views;

import simulator.control.Controller;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class BodiesTable extends JPanel {

    private BodiesTableModel bodiesTableModel;
    private Controller controller;

    public BodiesTable(Controller controller) {
        this.controller = controller;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.black, 2), "Bodies",
                TitledBorder.LEFT, TitledBorder.TOP));
        setPreferredSize(new Dimension(300, 200));
        init();
    }

    private void init() {
        bodiesTableModel = new BodiesTableModel(controller);
        JTable bodiesTable = new JTable(bodiesTableModel);
        bodiesTable.setShowGrid(false);
        JScrollPane tableScroll = new JScrollPane(bodiesTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.add(tableScroll);
    }
}
