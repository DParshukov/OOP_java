package gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Отвечает за сохранение и загрузку конфигурации окон приложения.
 * Хранит данные в файле robots_config.properties в домашнем каталоге пользователя.
 */
public class WindowStateManager {

    private static final String CONFIG_FILE =
            System.getProperty("user.home") + File.separator + "robots_config.properties";

    private final Properties properties = new Properties();

    /**
     * Загружает конфигурацию из файла.
     * Если файл не существует — просто оставляет properties пустым (нет сохранённого состояния).
     */
    public void load() {
        File file = new File(CONFIG_FILE);
        if (!file.exists()) {
            return;
        }
        try (FileInputStream in = new FileInputStream(file)) {
            properties.load(in);
        } catch (IOException e) {
            // Если файл повреждён — работаем без сохранённого состояния
        }
    }

    /**
     * Сохраняет конфигурацию в файл.
     */
    public void save() {
        try (FileOutputStream out = new FileOutputStream(CONFIG_FILE)) {
            properties.store(out, "Robots application window state");
        } catch (IOException e) {
            // Не удалось сохранить — ничего страшного, при следующем запуске будет дефолт
        }
    }

    /**
     * Записывает целочисленное значение по ключу prefix.key.
     */
    public void setInt(String prefix, String key, int value) {
        properties.setProperty(prefix + "." + key, String.valueOf(value));
    }

    /**
     * Читает целочисленное значение. Если ключа нет — возвращает defaultValue.
     */
    public int getInt(String prefix, String key, int defaultValue) {
        String value = properties.getProperty(prefix + "." + key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Записывает булево значение по ключу prefix.key.
     */
    public void setBoolean(String prefix, String key, boolean value) {
        properties.setProperty(prefix + "." + key, String.valueOf(value));
    }

    /**
     * Читает булево значение. Если ключа нет — возвращает defaultValue.
     */
    public boolean getBoolean(String prefix, String key, boolean defaultValue) {
        String value = properties.getProperty(prefix + "." + key);
        if (value == null) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value);
    }
}
