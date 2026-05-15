package gui;

import java.util.Timer;
import java.util.TimerTask;

import model.RobotModel;

public class RobotMovementController {

    private static final long TICK_MS = 10;

    private final Timer timer = new Timer("model-timer", true);

    public RobotMovementController(RobotModel model) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                model.tick(TICK_MS);
            }
        }, 0, TICK_MS);
    }

    public void stop() {
        timer.cancel();
    }
}
