package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Environment {

    private final Properties properties = new Properties();

    public Environment(String propertyFileName) {
        try (InputStream inputStream = Environment.class.getClassLoader().getResourceAsStream(propertyFileName)) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getProperty(String property) {
        return properties.getProperty(property);
    }
}
