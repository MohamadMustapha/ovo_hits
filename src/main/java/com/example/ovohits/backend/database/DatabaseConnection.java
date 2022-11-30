package com.example.ovohits.backend.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection connection = null;

    public static Connection getConnection() {
        if (connection != null) return connection;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost/OVO_Hits",
                    "root",
                    "pass-123");
        }
        catch (ClassNotFoundException | SQLException e) { throw new RuntimeException(e); }
        return connection;
    }
}
