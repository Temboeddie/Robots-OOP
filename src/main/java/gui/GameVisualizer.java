package gui;

import Controller.RobotController;

import javax.swing.*;
import java.awt.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;



/**
 * Визуализирует робота и его цель в JPanel.
 * Слушает обновления RobotModel и перерисовывает сцену соответствующим образом.
 */

public class GameVisualizer extends JPanel implements PropertyChangeListener {
    private final RobotModel model;

    public GameVisualizer(RobotModel model, RobotController robotcontroller) {
        this.model = model;
        setDoubleBuffered(true);
        setBackground(Color.WHITE);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                robotcontroller.updateTarget(e.getPoint());

            }
        });
        setDoubleBuffered(true);
        model.addPropertyChangeListener(this);

    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setTransform(new AffineTransform());

        drawRobot(g2d, round(model.getRobotPositionX()), round(model.getRobotPositionY()), model.getRobotDirection());


        g2d.setTransform(new AffineTransform());
        drawTarget(g2d, round(model.getTargetPositionX()), round(model.getTargetPositionY()));
    }

    private static int round(double value) {
        return (int) (value + 0.5);
    }

    private void drawRobot(Graphics2D g, int x, int y, double direction) {
        AffineTransform t = AffineTransform.getRotateInstance(direction, x, y);
        g.setTransform(t);
        g.setColor(Color.MAGENTA);
        fillOval(g, x, y, 30, 10);
        g.setColor(Color.BLACK);
        drawOval(g, x, y, 30, 10);
        g.setColor(Color.WHITE);
        fillOval(g, x + 10, y, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, x + 10, y, 5, 5);


    }

    private void drawTarget(Graphics2D g, int x, int y) {
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
        g.setTransform(t);
        g.setColor(Color.GREEN);
        fillOval(g, x, y, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, x, y, 5, 5);
    }

    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("robotPosition".equals(evt.getPropertyName())) {
            repaint();
        }
    }
}