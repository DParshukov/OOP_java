package gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import log.Logger;

public class WindowStateStore {

    private static final String CONFIG_FILE =
            System.getProperty("user.home") + File.separator + "robots_config.properties";

    private final Properties properties = new Properties();

    public boolean load() {
        File file = new File(CONFIG_FILE);
        if (!file.exists()) return false;
        try (FileInputStream in = new FileInputStream(file)) {
            properties.load(in);
            return true;
        } catch (IOException e) {
            Logger.debug("Не удалось загрузить конфигурацию: " + e.getMessage());
            return false;
        }
    }

    public void save() {
        try (FileOutputStream out = new FileOutputStream(CONFIG_FILE)) {
            properties.store(out, "Robots application window state");
        } catch (IOException e) {
            Logger.debug("Не удалось сохранить конфигурацию: " + e.getMessage());
        }
    }

    public int getInt(String key, int defaultValue) {
        String value = properties.getProperty(key);
        if (value == null) return defaultValue;
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            Logger.debug("Некорректное значение для ключа \"" + key + "\": " + value);
            return defaultValue;
        }
    }

    public void setInt(String key, int value) {
        properties.setProperty(key, String.valueOf(value));
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        String value = properties.getProperty(key);
        return value != null ? Boolean.parseBoolean(value) : defaultValue;
    }

    public void setBoolean(String key, boolean value) {
        properties.setProperty(key, String.valueOf(value));
    }
}
