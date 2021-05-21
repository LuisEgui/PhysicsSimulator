package simulator.view;

import simulator.control.Controller;
import simulator.factories.*;
import simulator.model.PhysicsSimulator;
import simulator.model.bodies.FluentBuilder.Body;
import simulator.model.forcelaws.ForceLaws;
import simulator.model.forcelaws.NewtonUniversalGravitation;
import simulator.views.BodiesTable;
import simulator.views.ControlPanel;
import simulator.views.StatusBar;
import simulator.views.Viewer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

class ControlPanelTest {

    public static class Mock extends JFrame {
        Toolkit screen = Toolkit.getDefaultToolkit();
        Dimension screenDimension = screen.getScreenSize();
        //Image icon = screen.getImage("./resources/icons/open.png");

        public Mock() {
            super("Testing Control Panel");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(screenDimension.width/2, screenDimension.height/2);
            setExtendedState(Frame.NORMAL);
            setLayout(new BorderLayout());
            setLocation(screenDimension.width/4, screenDimension.height/4);
            //this.setIconImage(icon);
        }
    }

    public static void main(String[] args) {
        // ControlPanel view
        ControlPanel controlPanel;
        BodiesTable bodiesTable;
        Viewer viewer;
        StatusBar statusBar;

        Mock mock;

        // Controller
        Controller controller;
        PhysicsSimulator physicsSimulator;
        List<Builder<? extends Body>> bodyBuilders;
        Factory<? extends Body> bodyFactory;
        List<Builder<? extends ForceLaws>> forceLawBuilders;
        Factory<ForceLaws> forceLawsFactory;
        NewtonUniversalGravitation nlug = new NewtonUniversalGravitation();

        physicsSimulator = new PhysicsSimulator(10000, nlug);
        bodyBuilders = new ArrayList<>();
        bodyBuilders.add(new MassLossingBodyBuilder());
        bodyBuilders.add(new BasicBodyBuilder());
        bodyFactory = new BodyBasedFactory(bodyBuilders);
        forceLawBuilders = new ArrayList<>();
        forceLawBuilders.add(new NewtonUniversalGravitationBuilder());
        forceLawBuilders.add(new NoForceBuilder());
        forceLawBuilders.add(new MovingTowardsFixedPointBuilder());
        forceLawsFactory = new ForceLawBasedFactory(forceLawBuilders);
        controller = new Controller(physicsSimulator, bodyFactory, forceLawsFactory);

        controlPanel = new ControlPanel(controller);
        bodiesTable = new BodiesTable(controller);
        viewer = new Viewer(controller);
        statusBar = new StatusBar(controller);

        mock = new Mock();
        JPanel bodiesAndView = new JPanel();
        bodiesAndView.setLayout(new GridLayout(2, 1));
        bodiesAndView.add(bodiesTable);
        bodiesAndView.add(viewer);
        mock.add(controlPanel, BorderLayout.PAGE_START);
        mock.add(bodiesAndView, BorderLayout.CENTER);
        mock.add(statusBar, BorderLayout.PAGE_END);

        SwingUtilities.invokeLater(() -> mock.setVisible(true));
    }
}
