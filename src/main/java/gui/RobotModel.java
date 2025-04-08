package gui;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import log.Logger;

/**
 * Представляет модель робота с отслеживанием положения, направления и цели.
 * Управляет логикой движения и уведомляет слушателей об изменении положения с помощью PropertyChangeSupport.
 */
public class RobotModel {
    private double robotPositionX = 100;
    private double robotPositionY = 100;
    private double robotDirection = 0;
    private double targetPositionX = 150;
    private double targetPositionY = 100;

    private static final double maxVelocity = 0.1;
    private static final double maxAngularVelocity = 0.001;

    private final PropertyChangeSupport support;

    public RobotModel() {
        this.support = new PropertyChangeSupport(this);
    }

    public double getRobotPositionX() {
        return robotPositionX;
    }
    public double getRobotPositionY() {
        return robotPositionY;
    }
    public double getRobotDirection() {
        return robotDirection;
    }
    public double getTargetPositionX() {
        return targetPositionX;
    }
    public double getTargetPositionY() {
        return targetPositionY;
    }

    public void setTargetPosition(double x, double y) {
        this.targetPositionX = x;
        this.targetPositionY = y;
    }

    /**
     * Обновляет положение и направление робота на основе цели.
     * Останавливается, если находится в пределах maxVelocity цели; в противном случае корректирует направление и движется.
     */
    public void update() {
        double distance = distance(robotPositionX, robotPositionY, targetPositionX, targetPositionY);
        if (distance < 0.5) {
            robotPositionX = targetPositionX;
            robotPositionY = targetPositionY;

            return;
        }
        double velocity = maxVelocity;
        double angleToTarget = angleTo(robotPositionX, robotPositionY, targetPositionX, targetPositionY);
        double angleDiff = angleToTarget - robotDirection;

        double angularVelocity = maxAngularVelocity * Math.signum(angleDiff);
        angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);


        if (Math.abs(angleDiff) < 0.1) {
            robotDirection = angleToTarget;
            angularVelocity = 0;
        }


        moveRobot(velocity, angularVelocity, 10);
    }

    /**
     * Перемещает робота на основе скорости и угловой скорости в течение продолжительности.
     * Обеспечивает движение, сокращая расстояние до цели, и уведомляет слушателей об изменении положения.
     * @param velocity линейная скорость (от 0 до maxVelocity)
     * @param angularVelocity угловая скорость (от -maxAngularVelocity до maxAngularVelocity)
     * @param duration продолжительность движения
     */
    private void moveRobot(double velocity, double angularVelocity, double duration) {
        double oldX = robotPositionX;
        double oldY = robotPositionY;
        double oldDirection = robotDirection;

        velocity = applyLimits(velocity, 0, maxVelocity);
        angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);


        if (Math.abs(angularVelocity) < 1e-6) {

            robotPositionX += velocity * duration * Math.cos(robotDirection);
            robotPositionY += velocity * duration * Math.sin(robotDirection);
        } else {

            double newDirection = robotDirection + angularVelocity * duration;
            double turnRadius = velocity / angularVelocity;

            robotPositionX += turnRadius * (Math.sin(newDirection) - Math.sin(robotDirection));
            robotPositionY -= turnRadius * (Math.cos(newDirection) - Math.cos(robotDirection));
            robotDirection = asNormalizedRadians(newDirection);

        }


        support.firePropertyChange("robotPosition", new double[]{oldX, oldY, oldDirection},
                new double[]{robotPositionX, robotPositionY, robotDirection});
    }

    private static double distance(double x1, double y1, double x2, double y2) {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    private static double angleTo(double fromX, double fromY, double toX, double toY) {
        double diffX = toX - fromX;
        double diffY = toY - fromY;
        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }

    private static double applyLimits(double value, double min, double max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

    private static double asNormalizedRadians(double angle) {
        while (angle < 0) angle += 2 * Math.PI;
        while (angle >= 2 * Math.PI) angle -= 2 * Math.PI;
        return angle;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }
}