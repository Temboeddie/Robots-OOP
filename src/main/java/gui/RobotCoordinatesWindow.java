package gui;

import LocalizationManager.LocalizationManager;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Внутренний фрейм, отображающий текущие координаты робота.
 * Обновляется динамически через PropertyChangeListener из RobotModel.
 */

public class RobotCoordinatesWindow extends JInternalFrame implements PropertyChangeListener {
    private final JLabel positionLabel;
    private final RobotModel model;
    private LocalizationManager localizationManager;

    public RobotCoordinatesWindow(RobotModel model,LocalizationManager localizationManager) {
        super(localizationManager.getString("window.coordinates"), true, true, true, true);
        this.model = model;
        setIconifiable(true);

        positionLabel = new JLabel("X: " + String.format("%.1f", model.getRobotPositionX()) +
                ", Y: " + String.format("%.1f", model.getRobotPositionY()));
        JPanel panel = new JPanel();
        panel.add(positionLabel);
        add(panel);

        setSize(200, 100);
        setLocation(320, 10);
        model.addPropertyChangeListener(this);

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("robotPosition".equals(evt.getPropertyName())) {
            double[] newPosition = (double[]) evt.getNewValue();
            positionLabel.setText("X: " + String.format("%.1f", newPosition[0]) +
                    ", Y: " + String.format("%.1f", newPosition[1]));
        }
    }


}