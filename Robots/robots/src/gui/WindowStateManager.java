package gui;

import java.beans.PropertyVetoException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JInternalFrame;

import log.Logger;

public class WindowStateManager {

    private final WindowStateStore store;
    private final Map<String, JInternalFrame> windows = new LinkedHashMap<>();

    public WindowStateManager(WindowStateStore store) {
        this.store = store;
    }

    public void registerAll(Map<String, JInternalFrame> windowMap) {
        windows.putAll(windowMap);
    }

    public void saveAll() {
        for (Map.Entry<String, JInternalFrame> entry : windows.entrySet()) {
            saveWindowState(entry.getValue(), entry.getKey());
        }
        store.save();
    }

    public void restoreAll() {
        for (Map.Entry<String, JInternalFrame> entry : windows.entrySet()) {
            restoreWindowState(entry.getValue(), entry.getKey());
        }
    }

    private void saveWindowState(JInternalFrame frame, String prefix) {
        store.setInt(prefix + ".x",         frame.getX());
        store.setInt(prefix + ".y",         frame.getY());
        store.setInt(prefix + ".width",     frame.getWidth());
        store.setInt(prefix + ".height",    frame.getHeight());
        store.setBoolean(prefix + ".iconified", frame.isIcon());
    }

    private void restoreWindowState(JInternalFrame frame, String prefix) {
        int x      = store.getInt(prefix + ".x",      frame.getX());
        int y      = store.getInt(prefix + ".y",      frame.getY());
        int width  = store.getInt(prefix + ".width",  frame.getWidth());
        int height = store.getInt(prefix + ".height", frame.getHeight());
        boolean icon = store.getBoolean(prefix + ".iconified", false);

        frame.setBounds(x, y, width, height);
        try {
            frame.setIcon(icon);
        } catch (PropertyVetoException e) {
            Logger.debug("Не удалось восстановить иконку окна \"" + prefix + "\": " + e.getMessage());
        }
    }
}
