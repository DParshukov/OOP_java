package gui;

import java.awt.Frame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import config.Config;
import config.ConfigStore;

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
            ConfigStore configStore = new ConfigStore();
            Config config = configStore.load();
            WindowStateManager stateManager = new WindowStateManager(config);
            MainApplicationFrame frame = new MainApplicationFrame(stateManager, configStore);
            frame.pack();
            frame.setVisible(true);
            frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        });
    }
}
