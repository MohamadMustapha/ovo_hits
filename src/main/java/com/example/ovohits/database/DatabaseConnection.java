package com.example.ovohits.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    public static Connection connection;

    public static Connection getConnection() {
        String url = "jdbc:mysql://localhost/OVO_Hits";
        String adminUsername = "root";
        String adminPassword = "pass-123";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, adminUsername, adminPassword);
        } catch (Exception e) { e.printStackTrace(); }

        return connection;
    }
}