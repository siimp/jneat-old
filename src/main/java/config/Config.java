package main.java.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * File path may be specified with system property main.java.config.path
 *
 */
public class Config {

    private static final String FILE_NAME = "/config.properties";
    private static final String FILE_NAME_SYSTEM_PROPERTY = "main.java.config.path";
    private static final Map<String, Object> CACHE = new HashMap<>();

    private static Config instance;

    private Properties properties = new Properties();

    private Config() {
    }

    public static Config getInstance() {
        if (instance == null) {
            CACHE.clear();
            initializeInstance();
        }
        return instance;
    }

    private static void initializeInstance() {
        instance = new Config();

        String systemFilePath = System.getProperty(FILE_NAME_SYSTEM_PROPERTY);
        try (final InputStream stream = Config.class.getResourceAsStream(systemFilePath != null ? systemFilePath : FILE_NAME)) {
            instance.properties.load(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double getDouble(ConfigValue key) {
        Double cachedValue = (Double) CACHE.get(key.toString());
        if (cachedValue != null) {
            return cachedValue;
        }
        double value = Double.parseDouble(getString(key));
        CACHE.put(key.toString(), value);
        return value;
    }
    
    public void setDouble(ConfigValue key, double value) {
        CACHE.put(key.toString(), value);
    }

    public int getInteger(ConfigValue key) {
        Integer cachedValue = (Integer) CACHE.get(key.toString());
        if (cachedValue != null) {
            return cachedValue;
        }
        int value = Integer.parseInt(getString(key));
        CACHE.put(key.toString(), value);
        return value;
    }

    public long getLong(ConfigValue key) {
        Long cachedValue = (Long) CACHE.get(key.toString());
        if (cachedValue != null) {
            return cachedValue;
        }
        long value = Long.parseLong(getString(key));
        CACHE.put(key.toString(), value);
        return value;
    }

    public String getString(ConfigValue key) {
        Object value = instance.properties.get(key.toString());
        if (value == null) {
            throw new RuntimeException("Config property \"" + key.toString() + "\" not found");
        }
        return value.toString();
    }

    public static void reset() {
        Config.instance = null;
    }

}
