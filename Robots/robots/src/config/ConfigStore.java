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

    private Config config;

    public Config load() {
        File file = new File(CONFIG_FILE);
        if (!file.exists()) {
            Logger.debug("Файл конфигурации не найден, конфиг будет пустым");
            config = new Config();
            return config;
        }
        Properties properties = new Properties();
        try (FileInputStream in = new FileInputStream(file)) {
            properties.load(in);
            config = new Config(properties);
            return config;
        } catch (IOException e) {
            Logger.debug("Не удалось загрузить конфигурацию: " + e.getMessage());
            config = new Config();
            return config;
        }
    }

    public void save() {
        try (FileOutputStream out = new FileOutputStream(CONFIG_FILE)) {
            config.getProperties().store(out, "Robots application window state");
        } catch (IOException e) {
            Logger.debug("Не удалось сохранить конфигурацию: " + e.getMessage());
        }
    }
}
