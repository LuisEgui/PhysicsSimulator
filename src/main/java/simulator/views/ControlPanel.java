package simulator.views;

import simulator.control.Controller;
import simulator.model.SimulatorObserver;
import simulator.model.bodies.FluentBuilder.Body;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.List;

public class ControlPanel extends JPanel implements SimulatorObserver {

    private Controller controller;
    private boolean stopped;
    private JToolBar toolBar;
    private static JButton fileButton;
    private static JButton lawButton;
    private static JButton exitButton;
    private static JButton runButton;
    private JFileChooser fileChooser;
    private ForceLawDialog forceLawDialog;
    private static JTextField deltaTimeText;
    private static JSpinner steps;

    public ControlPanel(Controller controller) {
        this.controller = controller;
        stopped = true;
        initGUI();
        this.controller.addObserver(this);
    }

    private void initGUI() {
        setLayout(new BorderLayout());

        toolBar = new JToolBar();
        toolBar.setFloatable(false);
        add(toolBar, BorderLayout.PAGE_START);

        createFileButton();
        toolBar.addSeparator();
        createLawButton();
        toolBar.addSeparator();
        createRunButton();
        toolBar.addSeparator();
        createStopButton();
        toolBar.addSeparator();
        createStepsText();
        toolBar.addSeparator();
        createDeltaTimeText();
        toolBar.addSeparator(new Dimension(400, 0));
        createExitButton();
    }

    private void createFileChooser() {
        fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Load a file with the bodies data");
        fileChooser.setCurrentDirectory(new File("./resources/examples/"));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileFilter(new FileNameExtensionFilter("JavaScript Object Notation (JSON)", "json"));
        fileChooser.setLocation(0,0);
    }

    private void createFileButton() {
        createFileChooser();

        fileButton = new JButton();
        fileButton.setToolTipText("Load a file");
        fileButton.setIcon(new ImageIcon("./resources/icons/open.png"));
        fileButton.addActionListener(actionEvent -> {
            if(fileChooser.showOpenDialog(ControlPanel.this) == JFileChooser.APPROVE_OPTION) {
                try (InputStream input = new FileInputStream(fileChooser.getSelectedFile())){
                    fileChooser.updateUI();
                    controller.reset();
                    controller.loadBodies(input);
                } catch (IOException exception) {
                    JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(ControlPanel.this),
                            "Error loading the file");
                    exception.printStackTrace();
                }
            } else
                JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(ControlPanel.this),
                        "Either you cancelled or there was an error");
        });

        toolBar.add(fileButton);
    }

    private void createLawButton() {
        lawButton = new JButton();
        lawButton.setToolTipText("Modify force law data");
        lawButton.setIcon(new ImageIcon("./resources/icons/physics.png"));
        lawButton.addActionListener(actionEvent ->
                forceLawDialog = new ForceLawDialog((Frame)SwingUtilities.getWindowAncestor(ControlPanel.this),
                        controller));
        toolBar.add(lawButton);
    }

    private void createStepsText() {
        JLabel stepsText = new JLabel("Steps: ", SwingConstants.CENTER);
        steps = new JSpinner(new SpinnerNumberModel(0, 0, 20000, 500));
        steps.setSize(70, 45);
        steps.setToolTipText("Set the steps to be run in the simulation execution");
        toolBar.add(stepsText);
        toolBar.add(steps);
    }

    private void runSim(int steps) {
        if(steps > 0 && !stopped) {
            try {
                controller.run(1);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "An error has occurred");
                enableAllButtons(true);
                stopped = true;
                return;
            }
            SwingUtilities.invokeLater(() -> runSim(steps-1));
        } else {
            stopped = true;
            enableAllButtons(true);
        }
    }

    private void createRunButton() {
        runButton = new JButton();
        runButton.setIcon(new ImageIcon("resources/icons/run.png"));
        runButton.setToolTipText("Run the simulation");
        runButton.addActionListener(actionEvent -> {
            enableAllButtons(false);
            stopped = false;
            int deltaTime;
            int numberSteps;
            try {
                deltaTime = Integer.parseInt(deltaTimeText.getText());
                numberSteps = Integer.parseInt(steps.getValue().toString());
                controller.setDeltaTime(deltaTime);
                runSim(numberSteps);
            } catch (NumberFormatException e) {
                //
            }
        });
        toolBar.add(runButton);
    }

    private void createDeltaTimeText() {
        JLabel deltaTimeLabel = new JLabel("Delta-Time: ", SwingConstants.CENTER);
        deltaTimeText = new JTextField(20);
        deltaTimeText.setSize(70, 45);
        deltaTimeText.setToolTipText("Number of calculation between each step");

        deltaTimeText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(allowedKeys(e.getKeyChar()))
                    deltaTimeText.setEditable(true);
                else
                    deltaTimeText.setEditable(false);
            }
        });

        toolBar.add(deltaTimeLabel);
        toolBar.add(deltaTimeText);
    }

    private static boolean allowedKeys(char key) {
        return ((key >= '0' && key <= '9') || key == 8
                || key == 46 || key == 127 || key == 26
                || key == 27);
    }

    private static void enableAllButtons(boolean enable) {
        fileButton.setEnabled(enable);
        lawButton.setEnabled(enable);
        runButton.setEnabled(enable);
        steps.setEnabled(enable);
        deltaTimeText.setEnabled(enable);
        exitButton.setEnabled(enable);
    }

    private void createExitButton() {
        exitButton = new JButton();
        exitButton.setIcon(new ImageIcon("resources/icons/exit.png"));
        exitButton.setToolTipText("Exit the program");
        exitButton.addActionListener(actionEvent -> {
            if(JOptionPane.showConfirmDialog(ControlPanel.this,
                    "Would you like to quit the program?",
                    "Exit Program",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION) {
                System.exit(0);
            }
        });

        toolBar.add(exitButton);
    }

    private void createStopButton() {
        JButton stopButton = new JButton();
        stopButton.setIcon(new ImageIcon("resources/icons/stop.png"));
        stopButton.setToolTipText("Stop the simulation execution");
        stopButton.addActionListener(actionEvent -> stopped = true);

        toolBar.add(stopButton);
    }

    @Override
    public void onRegister(List<? extends Body> bodies, double time, double dt, String fLawsDesc) {
        deltaTimeText.setText(String.valueOf((int) dt));
    }

    @Override
    public void onReset(List<? extends Body> bodies, double time, double dt, String fLawsDesc) {
        deltaTimeText.setText(String.valueOf((int) dt));
    }

    @Override
    public void onBodyAdded(List<? extends Body> bodies, Body b) {
        // Nothing to be aware of
    }

    @Override
    public void onAdvance(List<? extends Body> bodies, double time) {
        // Nothing to be aware of
    }

    @Override
    public void onDeltaTimeChanged(double dt) {
        // Nothing to be aware of
    }

    @Override
    public void onForceLawsChanged(String fLawsDesc) {
        // Nothing to be aware of
    }
}
