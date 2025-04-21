package Controller;

import gui.GameVisualizer;
import gui.RobotModel;

import javax.swing.*;
import java.awt.*;
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

    private final Timer timer;

    public RobotController(RobotModel model){
        this.model = model;
        this.timer = new Timer("event generator",true);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                model.update();


            }

            },0,20 );

    }
    public void updateTarget(Point point){

        model.setTargetPosition(point.x, point.y);
    }

}

