package gui;

import java.beans.PropertyVetoException;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

import config.Config;
import log.Logger;

public class WindowStateManager {

    private final Config config;

    public WindowStateManager(Config config) {
        this.config = config;
    }

    public void saveAll(JDesktopPane desktopPane) {
        for (JInternalFrame frame : desktopPane.getAllFrames()) {
            saveWindowState(frame, frame.getName());
        }
    }

    public void restoreAll(JDesktopPane desktopPane) {
        for (JInternalFrame frame : desktopPane.getAllFrames()) {
            restoreWindowState(frame, frame.getName());
        }
    }

    private void saveWindowState(JInternalFrame frame, String prefix) {
        config.setInt(prefix + ".x",         frame.getX());
        config.setInt(prefix + ".y",         frame.getY());
        config.setInt(prefix + ".width",     frame.getWidth());
        config.setInt(prefix + ".height",    frame.getHeight());
        config.setBoolean(prefix + ".iconified", frame.isIcon());
    }

    private void restoreWindowState(JInternalFrame frame, String prefix) {
        int x        = config.getInt(prefix + ".x",      frame.getX());
        int y        = config.getInt(prefix + ".y",      frame.getY());
        int width    = config.getInt(prefix + ".width",  frame.getWidth());
        int height   = config.getInt(prefix + ".height", frame.getHeight());
        boolean icon = config.getBoolean(prefix + ".iconified", false);

        frame.setBounds(x, y, width, height);
        try {
            frame.setIcon(icon);
        } catch (PropertyVetoException e) {
            Logger.debug("Не удалось восстановить иконку окна \"" + prefix + "\": " + e.getMessage());
        }
    }
}
