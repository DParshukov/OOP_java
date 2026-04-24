package gui;

import java.beans.PropertyVetoException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JInternalFrame;

import config.Config;
import log.Logger;

public class WindowStateManager {

    private final Config config;
    private final Map<String, JInternalFrame> windows = new HashMap<>();

    public WindowStateManager(Config config) {
        this.config = config;
    }

    public void register(String key, JInternalFrame frame) {
        windows.put(key, frame);
    }

    public void saveAll() {
        for (Map.Entry<String, JInternalFrame> entry : windows.entrySet()) {
            saveWindowState(entry.getValue(), entry.getKey());
        }
    }

    public void restoreAll() {
        for (Map.Entry<String, JInternalFrame> entry : windows.entrySet()) {
            restoreWindowState(entry.getValue(), entry.getKey());
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
