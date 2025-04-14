package Controller;

import gui.GameVisualizer;
import gui.RobotModel;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Управляет взаимодействием между RobotModel и GameVisualizer.
 * Обрабатывает пользовательский ввод и планирует обновления модели и перерисовки вида.
 */
public class RobotController {
    private final RobotModel model;
    private final GameVisualizer view;
    private final Timer timer;

    public RobotController(RobotModel model,GameVisualizer view){
        this.model = model;
        this.view = view;
        this.timer = new Timer("event generator",true);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                model.update();
                SwingUtilities.invokeLater(view::repaint);

            }

            },0,20 );
        view.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                model.setTargetPosition(e.getX(), e.getY());

            }
        });
    }

}