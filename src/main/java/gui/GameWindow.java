package gui;

import Controller.RobotController;

import java.awt.BorderLayout;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

public class GameWindow extends JInternalFrame
{

    private final RobotController controller;
    private final GameVisualizer visualizer;
    public GameWindow(RobotModel model)
    {
        super("Игровое поле", true, true, true, true);
        setIconifiable(true);

        this.controller = new RobotController(model);
        this.visualizer = new GameVisualizer(model,controller);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        setSize(400, 400);
    }
}
