package config;

import java.util.Properties;

public class Config {

    private final Properties properties;

    Config(Properties properties) {
        this.properties = properties;
    }

    public Config() {
        this.properties = new Properties();
    }

    public int getInt(String key, int defaultValue) {
        String value = properties.getProperty(key);
        if (value == null) return defaultValue;
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
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

    Properties getProperties() {
        return properties;
    }
}
