package simulator.views;

import simulator.control.Controller;
import simulator.misc.Vector2D;
import simulator.model.SimulatorObserver;
import simulator.model.bodies.FluentBuilder.Body;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class Viewer extends JComponent implements SimulatorObserver {

    private static final int WIDTH = 1000;
    private static final int HEIGHT = 1000;
    private int centerX;
    private int centerY;
    private double scale;
    private List<? extends Body> bodies;
    private boolean showHelp;
    private boolean showVectors;

    public Viewer(Controller controller) {
        controller.addObserver(this);
        initGUI();
    }

    private void initGUI() {
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.black, 2), "Viewer",
                TitledBorder.LEFT, TitledBorder.TOP));
        bodies = new ArrayList<>();
        scale = 1; showHelp = true; showVectors = true;
        setSize(WIDTH, HEIGHT);

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyChar()) {
                    case '-':
                        scale = scale * 1.1; repaint();
                        break;
                    case '+':
                        scale = Math.max(1000.0, scale / 1.1);
                        repaint();
                        break;
                    case '=': autoScale();
                        repaint();
                        break;
                    case 'h':
                        showHelp = !showHelp;
                        repaint();
                        break;
                    case 'v':
                        showVectors = !showVectors;
                        repaint();
                        break;
                    default:
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                requestFocus();
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        repaint();
    }

    private void autoScale() {
        double max = 1.0;
        for(Body b: bodies) {
            Vector2D p = b.getPosition();
            max = Math.max(max, Math.abs(p.getX()));
            max = Math.max(max, Math.abs(p.getY()));
        }
        double size = Math.max(1.0, Math.min((double) getWidth(),
                                (double) getHeight()));
        scale = max > size ? 4.0 * max/size : 1.0;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics = (Graphics2D) g;

        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Usamos 'graphics' para dibujar, no ’g’
        centerX = getWidth()/2;
        centerY = getHeight()/2;

        // Dibujamos una cruz en el centro
        graphics.setColor(Color.RED);
        graphics.drawLine(centerX, centerY + 5, centerX, centerY - 5);
        graphics.drawLine(centerX + 5, centerY, centerX - 5, centerY);

        // Dibujamos los bodies
        drawBodies(graphics);

        // Dibujamos el show help
        if(showHelp) {
            graphics.setColor(Color.RED);
            graphics.drawString("h: toggle help, v: toggle vectors, +: zoom-in, -: zoom-out, =: fit", 10, 30);
            graphics.drawString("Scaling ratio: " + scale, 10, 50);
        }

    }

    private void drawLineWithArrow(Graphics g, int x1, int y1, int x2, int y2, int w, int h,
                                   Color lineColor, Color arrowColor) {
        int dx = x2 - x1;
        int dy = y2 - y1;
        double D = Math.sqrt(dx * dx + dy * dy);
        double xm = D - w;
        double xn = xm;
        double ym = h;
        double yn = -h;
        double x;
        double sin = dy/D;
        double cos = dx/D;

        x = xm * cos - ym * sin + x1;
        ym = xm * sin + ym * cos + y1;
        xm = x;

        x = xn * cos - yn * sin + x1;
        yn = xn * sin + yn * cos + y1;
        xn = x;

        int[] xpoints = { x2, (int) xm, (int) xn};
        int[] ypoints = { y2, (int) ym, (int) yn};

        g.setColor(lineColor);
        g.drawLine(x1, y1, x2, y2);
        g.setColor(arrowColor);
        g.fillPolygon(xpoints, ypoints, 3);
    }

    private void drawBodies(Graphics g) {
        Vector2D position, velocity, force;
        int x, y;

        for(int i = 0; i < bodies.size(); i++) {
            position = bodies.get(i).getPosition();
            velocity = bodies.get(i).getVelocity().unitVector().scale(25);
            force = bodies.get(i).getForce().unitVector().scale(20);
            x = centerX + (int) (position.getX() / scale);
            y = centerY + (int) (position.getY() / scale);

            String id = bodies.get(i).getId();

            if (showVectors) {
                int x2 = x + (int) (velocity.getX());
                int y2 = y - (int) (velocity.getY());
                int x1 = x + (int) (force.getX());
                int y1 = y - (int) (force.getY());
                drawLineWithArrow(g, x, y, x1, y1, 5, 5, Color.RED, Color.RED);
                drawLineWithArrow(g, x, y, x2, y2, 5, 5, Color.GREEN, Color.GREEN);
                drawBody(g, id, x, y);
            }
        }
    }

    private void drawBody(Graphics g, String bodyId, int x, int y) {
        g.setColor(Color.black);
        g.drawString(bodyId, x, y - 10);
        g.setColor(Color.BLUE);
        g.fillOval(x - 5, y - 5, 10, 10);
    }

    @Override
    public void onRegister(List<? extends Body> bodies, double time, double dt, String fLawsDesc) {
        this.bodies = bodies;
        autoScale();
        repaint();
    }

    @Override
    public void onReset(List<? extends Body> bodies, double time, double dt, String fLawsDesc) {
        this.bodies = bodies;
        autoScale();
        repaint();
    }

    @Override
    public void onBodyAdded(List<? extends Body> bodies, Body b) {
        this.bodies = bodies;
        autoScale();
        repaint();
    }

    @Override
    public void onAdvance(List<? extends Body> bodies, double time) {
        this.bodies = bodies;
        repaint();
    }

    @Override
    public void onDeltaTimeChanged(double dt) {
    }

    @Override
    public void onForceLawsChanged(String fLawsDesc) {
    }
}
