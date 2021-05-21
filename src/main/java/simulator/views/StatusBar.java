package simulator.views;

import simulator.control.Controller;
import simulator.model.SimulatorObserver;
import simulator.model.bodies.FluentBuilder.Body;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StatusBar extends JPanel implements SimulatorObserver {

    private JLabel currentTime;
    private JLabel currentLaws;
    private JLabel numberOfBodies;

    public StatusBar(Controller controller) {
        initGUI();
        controller.addObserver(this);
    }

    private void initGUI() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBorder(BorderFactory.createBevelBorder(1));

        JLabel timeLabel = new JLabel("Time: ", JLabel.LEFT);
        add(timeLabel);
        currentTime = new JLabel("");
        add(currentTime);
        add(new JSeparator(SwingConstants.HORIZONTAL));

        JLabel bodiesLabel = new JLabel("Bodies: ", JLabel.LEFT);
        numberOfBodies = new JLabel("");
        add(bodiesLabel);
        add(numberOfBodies);
        add(new JSeparator(SwingConstants.HORIZONTAL));

        JLabel lawsLabel = new JLabel("Laws: ", JLabel.LEFT);
        currentLaws = new JLabel();
        add(lawsLabel);
        add(currentLaws);
    }

    @Override
    public void onRegister(List<? extends Body> bodies, double time, double dt, String fLawsDesc) {
        currentTime.setText("" + time);
        numberOfBodies.setText(""  + bodies.size());
        currentLaws.setText("" + fLawsDesc);
    }

    @Override
    public void onReset(List<? extends Body> bodies, double time, double dt, String fLawsDesc) {
        currentTime.setText("" + time);
        numberOfBodies.setText("" + bodies.size());
        currentLaws.setText("" + fLawsDesc);
    }

    @Override
    public void onBodyAdded(List<? extends Body> bodies, Body b) {
        numberOfBodies.setText("" + bodies.size());
    }

    @Override
    public void onAdvance(List<? extends Body> bodies, double time) {
        currentTime.setText("" + time);
    }

    @Override
    public void onDeltaTimeChanged(double dt) {
    }

    @Override
    public void onForceLawsChanged(String fLawsDesc) {
        currentLaws.setText("" + fLawsDesc);
    }
}
