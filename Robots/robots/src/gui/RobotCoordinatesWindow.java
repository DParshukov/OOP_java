package gui;

import java.awt.EventQueue;

import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import model.RobotModel;
import model.RobotModelListener;

public class RobotCoordinatesWindow extends JInternalFrame implements RobotModelListener {

    private final RobotModel model;
    private final DefaultTableModel tableModel;

    private static final String[] COLUMNS = {"Параметр", "Значение"};
    private static final String[] ROWS = {
        "Робот X", "Робот Y", "Направление (рад)", "Цель X", "Цель Y"
    };

    public RobotCoordinatesWindow(RobotModel model) {
        super("Координаты робота", true, true, true, true);
        this.model = model;
        model.registerListener(this);

        tableModel = new DefaultTableModel(COLUMNS, ROWS.length) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        for (int i = 0; i < ROWS.length; i++) {
            tableModel.setValueAt(ROWS[i], i, 0);
        }

        JTable table = new JTable(tableModel);
        table.setRowHeight(22);
        table.getColumnModel().getColumn(0).setPreferredWidth(160);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);

        getContentPane().add(new JScrollPane(table));
        pack();
        setSize(300, 160);
        updateAll();
    }

    @Override
    public void onRobotMoved() {
        EventQueue.invokeLater(this::updateRobot);
    }

    @Override
    public void onTargetChanged() {
        EventQueue.invokeLater(this::updateTarget);
    }

    private void updateRobot() {
        tableModel.setValueAt(String.format("%.1f", model.getRobotX()), 0, 1);
        tableModel.setValueAt(String.format("%.1f", model.getRobotY()), 1, 1);
        tableModel.setValueAt(String.format("%.3f", model.getRobotDirection()), 2, 1);
    }

    private void updateTarget() {
        tableModel.setValueAt(String.valueOf(model.getTargetX()), 3, 1);
        tableModel.setValueAt(String.valueOf(model.getTargetY()), 4, 1);
    }

    private void updateAll() {
        updateRobot();
        updateTarget();
    }
}
