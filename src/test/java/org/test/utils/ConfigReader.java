package org.test.utils;

import java.io.FileInputStream;
import java.util.Properties;

public class ConfigReader {
    private static Properties config = new Properties();
    private static Properties secrets = new Properties();

    static {
        try {
            config.load(new FileInputStream("src/test/resources/config.properties"));
            secrets.load(new FileInputStream("src/test/resources/secrets.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return config.getProperty(key);
    }

    public static String getSecret(String key) {
        return secrets.getProperty(key);
    }
}
