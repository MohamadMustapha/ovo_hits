package com.example.ovohits.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection connection = null;

    public static Connection getConnection() {
        if (connection != null) return connection;
        String url = "jdbc:mysql://localhost/OVO_Hits",
               adminUsername = "root",
               adminPassword = "pass-123";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, adminUsername, adminPassword);
        }
        catch (ClassNotFoundException | SQLException e) { throw new RuntimeException(e); }
        return connection;
    }
}
