package org.example.incrementer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OptimisticConcurrencyControlIncrementer extends CounterIncrementer {

    @Override
    public void increment() {
        try (Connection connection = super.getConnection()) {
            connection.setAutoCommit(false);

            PreparedStatement selectStatement = connection.prepareStatement(
                    "SELECT counter, version FROM user_counter WHERE user_id = 1");
            PreparedStatement updateStatement = connection.prepareStatement(
                    "UPDATE user_counter set counter = ?, version = ? where user_id = 1 and version = ?");

            for (int i = 0; i < 10_000; i++) {
                while (true) {
                    ResultSet resultSet = selectStatement.executeQuery();
                    resultSet.next();
                    int counter = resultSet.getInt("counter");
                    int version = resultSet.getInt("version");
                    resultSet.close();

                    updateStatement.setInt(1, ++counter);
                    updateStatement.setInt(2, version + 1);
                    updateStatement.setInt(3, version);
                    int rowCount = updateStatement.executeUpdate();

                    connection.commit();

                    if (rowCount > 0) break;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
