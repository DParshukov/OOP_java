package gui;

import java.awt.EventQueue;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.RobotModel;
import model.RobotModelListener;

public class RobotCoordinatesWindow extends JInternalFrame implements RobotModelListener {

    private final RobotModel model;
    private final JLabel labelX = new JLabel();
    private final JLabel labelY = new JLabel();
    private final JLabel labelDir = new JLabel();

    public RobotCoordinatesWindow(RobotModel model) {
        super("Координаты робота", true, true, true, true);
        this.model = model;
        model.registerListener(this);

        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        panel.add(new JLabel("X:"));
        panel.add(labelX);
        panel.add(new JLabel("Y:"));
        panel.add(labelY);
        panel.add(new JLabel("Направление (рад):"));
        panel.add(labelDir);

        getContentPane().add(panel);
        pack();
        updateLabels();
    }

    @Override
    public void onRobotMoved() {
        EventQueue.invokeLater(this::updateLabels);
    }

    private void updateLabels() {
        labelX.setText(String.format("%.1f", model.getRobotX()));
        labelY.setText(String.format("%.1f", model.getRobotY()));
        labelDir.setText(String.format("%.3f", model.getRobotDirection()));
    }
}
