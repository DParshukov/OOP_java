package model;

public final class RobotSnapshot {

    private final double robotX;
    private final double robotY;
    private final double robotDirection;
    private final int targetX;
    private final int targetY;

    public RobotSnapshot(double robotX, double robotY, double robotDirection,
                         int targetX, int targetY) {
        this.robotX = robotX;
        this.robotY = robotY;
        this.robotDirection = robotDirection;
        this.targetX = targetX;
        this.targetY = targetY;
    }

    public double getRobotX() { return robotX; }
    public double getRobotY() { return robotY; }
    public int getRobotXInt() { return (int) (robotX + 0.5); }
    public int getRobotYInt() { return (int) (robotY + 0.5); }
    public double getRobotDirection() { return robotDirection; }
    public int getTargetX() { return targetX; }
    public int getTargetY() { return targetY; }
}
