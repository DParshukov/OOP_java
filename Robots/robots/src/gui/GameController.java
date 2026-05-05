package gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import model.RobotModel;

public class GameController {

    public GameController(RobotModel model, GameVisualizer visualizer) {
        visualizer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                model.setTarget(e.getX(), e.getY());
            }
        });
    }
}
