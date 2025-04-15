package Controller;

import gui.GameVisualizer;
import gui.RobotModel;
import log.LogLevel;
import log.LogWindowSource;

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
    private final LogWindowSource logSource;

    public RobotController(RobotModel model,GameVisualizer view,LogWindowSource logSource){
        this.model = model;
        this.view = view;
        this.logSource = logSource;
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
                logSource.append(LogLevel.Info,"New target set: (" + e.getX() + ", " + e.getY() + ")");

            }
        });
    }

}