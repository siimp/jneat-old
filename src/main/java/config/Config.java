package main.java.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * File path may be specified with system property main.java.config.path
 *
 */
public class Config {

    private static final String FILE_NAME = "/config.properties";
    private static final String FILE_NAME_SYSTEM_PROPERTY = "main.java.config.path";

    private static Config instance;
    private final Properties properties = new Properties();

    protected Config() {
    }

    public static Config getInstance() {
        if (instance == null) {
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

    public static Object get(String key) {
        return Config.getInstance().properties.get(key);
    }

    public static double getDouble(ConfigValue key) {
        return Double.parseDouble(getString(key));
    }
    
    public static void setDouble(ConfigValue key, double value) {
        Config.getInstance().properties.put(key.toString(), value);
    }

    public static String getString(ConfigValue key) {
        Object value = Config.get(key.toString());
        if (value == null) {
            throw new RuntimeException("Config property \"" + key.toString() + "\" not found");
        }
        return value.toString();
    }

    public static int getInteger(ConfigValue key) {
        return Integer.parseInt(getString(key));
    }

    public static long getLong(ConfigValue key) {
        return Long.parseLong(getString(key));
    }

    public static void reset() {
        Config.instance = null;
    }

}
