package org.example.incrementer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InPlaceIncrementer extends CounterIncrementer {

    @Override
    public void increment() {
        try (Connection connection = super.getConnection()) {
            connection.setAutoCommit(false);
            PreparedStatement updateStatement = connection.prepareStatement(
                    "UPDATE user_counter SET counter = counter + 1 WHERE user_id = 1");

            for (int i = 0; i < 10_000; i++) {
                updateStatement.executeUpdate();
                connection.commit();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
