package gui;

import Controller.RobotController;

import java.awt.BorderLayout;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

public class GameWindow extends JInternalFrame
{
    private final RobotController controller;

    public GameWindow(RobotModel model)
    {
        super("Игровое поле", true, true, true, true);
        setIconifiable(true);
        GameVisualizer visualizer = new GameVisualizer(model);
        this.controller = new RobotController(model, visualizer);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        setSize(400, 400);
    }
}
