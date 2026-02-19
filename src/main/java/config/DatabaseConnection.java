package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String url = "jdbc:postgresql://localhost:5432/push_down_appro";
    private static final String user = "mon_user";
    private static final String password = "mon_password";

    public Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, user, password);

        } catch (SQLException e) {
            System.out.println("Error" + e.getMessage());
        }
        return connection;
    }
}
