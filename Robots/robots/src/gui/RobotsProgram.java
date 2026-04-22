package gui;

import log.Logger;

import java.awt.Frame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class RobotsProgram {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        UIManager.put("OptionPane.yesButtonText",    "Да");
        UIManager.put("OptionPane.noButtonText",     "Нет");
        UIManager.put("OptionPane.cancelButtonText", "Отмена");
        UIManager.put("OptionPane.okButtonText",     "ОК");

        SwingUtilities.invokeLater(() -> {
            WindowStateStore store = new WindowStateStore();
            boolean loaded = store.load();
            if (!loaded) {
                Logger.debug("Файл конфигурации не найден, используются значения по умолчанию");
            }
            WindowStateManager stateManager = new WindowStateManager(store);
            MainApplicationFrame frame = new MainApplicationFrame(stateManager);
            frame.pack();
            frame.setVisible(true);
            frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        });
    }
}
