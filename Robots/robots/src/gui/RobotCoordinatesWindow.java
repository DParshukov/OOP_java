package gui;

import java.awt.EventQueue;

import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import model.RobotModelListener;
import model.RobotSnapshot;

public class RobotCoordinatesWindow extends JInternalFrame implements RobotModelListener {

    private final DefaultTableModel tableModel;

    private static final String[] COLUMNS = {"Параметр", "Значение"};
    private static final String[] ROWS = {
        "Робот X", "Робот Y", "Направление (рад)", "Цель X", "Цель Y"
    };

    public RobotCoordinatesWindow() {
        super("Координаты робота", true, true, true, true);

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
    }

    @Override
    public void onRobotMoved(RobotSnapshot s) {
        EventQueue.invokeLater(() -> updateRobot(s));
    }

    @Override
    public void onTargetChanged(RobotSnapshot s) {
        EventQueue.invokeLater(() -> updateTarget(s));
    }

    private void updateRobot(RobotSnapshot s) {
        tableModel.setValueAt(String.format("%.1f", s.getRobotX()), 0, 1);
        tableModel.setValueAt(String.format("%.1f", s.getRobotY()), 1, 1);
        tableModel.setValueAt(String.format("%.3f", s.getRobotDirection()), 2, 1);
    }

    private void updateTarget(RobotSnapshot s) {
        tableModel.setValueAt(String.valueOf(s.getTargetX()), 3, 1);
        tableModel.setValueAt(String.valueOf(s.getTargetY()), 4, 1);
    }
}
