package org.example.incrementer;

import org.example.Environment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class CounterIncrementer implements Runnable {

    @Override
    public void run() {
        increment();
    }

    public abstract void increment();

    protected Connection getConnection() throws SQLException {
        Environment environment = new Environment("application.properties");

        String url = environment.getProperty("datasource.url");
        String user = environment.getProperty("datasource.user");
        String password = environment.getProperty("datasource.password");

        return DriverManager.getConnection(url, user, password);
    }
}
