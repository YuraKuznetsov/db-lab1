package org.example.incrementer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RowLevelLockingIncrementer extends CounterIncrementer {

    @Override
    public void increment() {
        try (Connection connection = super.getConnection()) {
            connection.setAutoCommit(false);

            PreparedStatement selectStatement = connection.prepareStatement(
                    "SELECT counter FROM user_counter WHERE user_id = 1 FOR UPDATE");
            PreparedStatement updateStatement = connection.prepareStatement(
                    "UPDATE user_counter SET counter = ? WHERE user_id = 1");

            for (int i = 0; i < 10_000; i++) {
                ResultSet resultSet = selectStatement.executeQuery();
                resultSet.next();
                int counter = resultSet.getInt("counter");
                resultSet.close();

                updateStatement.setInt(1, ++counter);
                updateStatement.executeUpdate();

                connection.commit();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
