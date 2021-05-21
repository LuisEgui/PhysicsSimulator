package simulator.views;

import simulator.control.Controller;
import simulator.model.SimulatorObserver;
import simulator.model.bodies.FluentBuilder.Body;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class BodiesTableModel extends AbstractTableModel implements SimulatorObserver {

    private List<? extends Body> bodies;
    private String[] columnNames = {"Id", "Mass", "Position", "Velocity", "Force"};
    private Controller controller;

    public BodiesTableModel(Controller controller) {
        this.controller = controller;
        bodies = new ArrayList<>();
        controller.addObserver(this);
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public int getRowCount() {
        return bodies == null ? 0 : bodies.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object object = null;

        switch (columnIndex) {
            case 0:
                object = bodies.get(rowIndex).getId();
                break;
            case 1:
                object = bodies.get(rowIndex).getMass();
                break;
            case 2:
                object = bodies.get(rowIndex).getPosition();
                break;
            case 3:
                object = bodies.get(rowIndex).getVelocity();
                break;
            case 4:
                object = bodies.get(rowIndex).getForce();
                break;
        }

        return object;
    }

    @Override
    public void onRegister(List<? extends Body> bodies, double time, double dt, String fLawsDesc) {
        this.bodies = bodies;
        fireTableDataChanged();
    }

    @Override
    public void onReset(List<? extends Body> bodies, double time, double dt, String fLawsDesc) {
        this.bodies = bodies;
        fireTableDataChanged();
    }

    @Override
    public void onBodyAdded(List<? extends Body> bodies, Body b) {
        this.bodies = bodies;
        fireTableDataChanged();
    }

    @Override
    public void onAdvance(List<? extends Body> bodies, double time) {
        this.bodies = bodies;
        fireTableDataChanged();
    }

    @Override
    public void onDeltaTimeChanged(double dt) {
    }

    @Override
    public void onForceLawsChanged(String fLawsDesc) {
    }
}
