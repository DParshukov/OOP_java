package config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import log.Logger;

public class ConfigStore {

    private static final String CONFIG_FILE =
            System.getProperty("user.home") + File.separator + "robots_config.properties";

    public Config load() {
        File file = new File(CONFIG_FILE);
        if (!file.exists()) {
            Logger.debug("Файл конфигурации не найден, используются значения по умолчанию");
            return new Config();
        }
        Properties properties = new Properties();
        try (FileInputStream in = new FileInputStream(file)) {
            properties.load(in);
            return new Config(properties);
        } catch (IOException e) {
            Logger.debug("Не удалось загрузить конфигурацию: " + e.getMessage());
            return new Config();
        }
    }

    public void save(Config config) {
        try (FileOutputStream out = new FileOutputStream(CONFIG_FILE)) {
            config.getProperties().store(out, "Robots application window state");
        } catch (IOException e) {
            Logger.debug("Не удалось сохранить конфигурацию: " + e.getMessage());
        }
    }
}
