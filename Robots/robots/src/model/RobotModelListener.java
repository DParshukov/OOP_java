package model;

public interface RobotModelListener {
    void onRobotMoved(RobotSnapshot snapshot);
    default void onTargetChanged(RobotSnapshot snapshot) {}
}
