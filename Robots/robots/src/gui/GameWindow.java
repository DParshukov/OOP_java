package gui;

import java.awt.BorderLayout;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import model.RobotModel;

public class GameWindow extends JInternalFrame {

    public GameWindow(RobotModel model) {
        super("Игровое поле", true, true, true, true);
        GameVisualizer visualizer = new GameVisualizer(model);
        new GameController(model, visualizer);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        setSize(400, 400);
    }
}
