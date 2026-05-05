package model;

public interface RobotModelListener {
    void onRobotMoved();
    default void onTargetChanged() {}
}
