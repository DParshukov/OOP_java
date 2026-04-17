package gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class RobotModel {

    private volatile double robotX = 100;
    private volatile double robotY = 100;
    private volatile double robotDirection = 0;

    private volatile int targetX = 150;
    private volatile int targetY = 100;

    private static final double MAX_VELOCITY = 0.1;
    private static final double MAX_ANGULAR_VELOCITY = 0.001;

    private final List<RobotModelListener> listeners = new ArrayList<>();
    private volatile RobotModelListener[] activeListeners = new RobotModelListener[0];

    private final Timer timer = new Timer("model-timer", true);

    public RobotModel() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                update();
            }
        }, 0, 10);
    }

    public void setTarget(int x, int y) {
        targetX = x;
        targetY = y;
    }

    public double getRobotX() { return robotX; }
    public double getRobotY() { return robotY; }
    public double getRobotDirection() { return robotDirection; }
    public int getTargetX() { return targetX; }
    public int getTargetY() { return targetY; }

    public void registerListener(RobotModelListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
            activeListeners = null;
        }
    }

    public void unregisterListener(RobotModelListener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
            activeListeners = null;
        }
    }

    private void notifyListeners() {
        RobotModelListener[] current = activeListeners;
        if (current == null) {
            synchronized (listeners) {
                if (activeListeners == null) {
                    activeListeners = listeners.toArray(new RobotModelListener[0]);
                }
                current = activeListeners;
            }
        }
        for (RobotModelListener l : current) {
            l.onRobotMoved();
        }
    }

    private void update() {
        double dist = distance(targetX, targetY, robotX, robotY);
        if (dist < 0.5) return;

        double angleToTarget = angleTo(robotX, robotY, targetX, targetY);

        // Вычисляем кратчайшую дугу в (-π, π], чтобы избежать бага с wrap-around
        double diff = angleToTarget - robotDirection;
        diff = ((diff + Math.PI) % (2 * Math.PI) + 2 * Math.PI) % (2 * Math.PI) - Math.PI;

        double angularVelocity = 0;
        if (Math.abs(diff) > 1e-6) {
            angularVelocity = diff > 0 ? MAX_ANGULAR_VELOCITY : -MAX_ANGULAR_VELOCITY;
        }

        move(MAX_VELOCITY, angularVelocity, 10);
        notifyListeners();
    }

    private void move(double velocity, double angularVelocity, double duration) {
        velocity = clamp(velocity, 0, MAX_VELOCITY);
        angularVelocity = clamp(angularVelocity, -MAX_ANGULAR_VELOCITY, MAX_ANGULAR_VELOCITY);

        double newX = robotX + velocity / angularVelocity *
                (Math.sin(robotDirection + angularVelocity * duration) - Math.sin(robotDirection));
        if (!Double.isFinite(newX)) {
            newX = robotX + velocity * duration * Math.cos(robotDirection);
        }

        double newY = robotY - velocity / angularVelocity *
                (Math.cos(robotDirection + angularVelocity * duration) - Math.cos(robotDirection));
        if (!Double.isFinite(newY)) {
            newY = robotY + velocity * duration * Math.sin(robotDirection);
        }

        robotX = newX;
        robotY = newY;
        robotDirection = normalizeAngle(robotDirection + angularVelocity * duration);
    }

    private static double distance(double x1, double y1, double x2, double y2) {
        double dx = x1 - x2;
        double dy = y1 - y2;
        return Math.sqrt(dx * dx + dy * dy);
    }

    private static double angleTo(double fromX, double fromY, double toX, double toY) {
        return normalizeAngle(Math.atan2(toY - fromY, toX - fromX));
    }

    private static double normalizeAngle(double angle) {
        while (angle < 0) angle += 2 * Math.PI;
        while (angle >= 2 * Math.PI) angle -= 2 * Math.PI;
        return angle;
    }

    private static double clamp(double value, double min, double max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }
}
