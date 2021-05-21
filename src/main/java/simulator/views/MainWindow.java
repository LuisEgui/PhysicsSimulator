package simulator.views;

import simulator.control.Controller;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    private Controller controller;
    private Toolkit screen = Toolkit.getDefaultToolkit();
    private Dimension screenDimension = screen.getScreenSize();
    private int altura = screenDimension.height;
    private int ancho = screenDimension.width;

    public MainWindow(Controller controller) {
        super("Physics Simulator");
        this.controller = controller;
        setVisible(true);
        setSize(ancho/2, altura/2);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());
        setExtendedState(Frame.NORMAL);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initGUI();
    }

    private void initGUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        setContentPane(mainPanel);

        ControlPanel controlPanel = new ControlPanel(controller);
        BodiesTable bodiesTable = new BodiesTable(controller);
        Viewer viewer = new Viewer(controller);
        StatusBar statusBar = new StatusBar(controller);

        JPanel bodiesAndView = new JPanel();
        bodiesAndView.setLayout(new GridLayout(2, 1));
        bodiesAndView.add(bodiesTable);
        bodiesAndView.add(viewer);

        mainPanel.add(controlPanel, BorderLayout.PAGE_START);
        mainPanel.add(bodiesAndView, BorderLayout.CENTER);
        mainPanel.add(statusBar, BorderLayout.PAGE_END);
    }
}
