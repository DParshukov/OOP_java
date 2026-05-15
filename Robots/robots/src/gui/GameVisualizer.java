package gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

import model.RobotModelListener;
import model.RobotSnapshot;

public class GameVisualizer extends JPanel implements RobotModelListener {

    private volatile RobotSnapshot snapshot;
    private final Timer redrawTimer = new Timer("redraw-timer", true);

    public GameVisualizer() {
        redrawTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                EventQueue.invokeLater(GameVisualizer.this::repaint);
            }
        }, 0, 50);

        setDoubleBuffered(true);
    }

    @Override
    public void onRobotMoved(RobotSnapshot s) {
        snapshot = s;
    }

    @Override
    public void onTargetChanged(RobotSnapshot s) {
        snapshot = s;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        RobotSnapshot s = snapshot;
        if (s == null) return;
        Graphics2D g2d = (Graphics2D) g;
        drawRobot(g2d, s.getRobotXInt(), s.getRobotYInt(), s.getRobotDirection());
        drawTarget(g2d, s.getTargetX(), s.getTargetY());
    }

    private void drawRobot(Graphics2D g, int x, int y, double direction) {
        AffineTransform t = AffineTransform.getRotateInstance(direction, x, y);
        g.setTransform(t);
        g.setColor(Color.MAGENTA);
        fillOval(g, x, y, 30, 10);
        g.setColor(Color.BLACK);
        drawOval(g, x, y, 30, 10);
        g.setColor(Color.WHITE);
        fillOval(g, x + 10, y, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, x + 10, y, 5, 5);
    }

    private void drawTarget(Graphics2D g, int x, int y) {
        g.setTransform(new AffineTransform());
        g.setColor(Color.GREEN);
        fillOval(g, x, y, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, x, y, 5, 5);
    }

    private static void fillOval(Graphics g, int cx, int cy, int w, int h) {
        g.fillOval(cx - w / 2, cy - h / 2, w, h);
    }

    private static void drawOval(Graphics g, int cx, int cy, int w, int h) {
        g.drawOval(cx - w / 2, cy - h / 2, w, h);
    }
}
