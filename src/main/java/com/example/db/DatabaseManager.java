package com.example.db;

import java.sql.*;
import java.nio.file.*;
import java.io.File;

public class DatabaseManager {
    // Define database location relative to project root
    private static final String DB_FILE = "cafe_reviews.db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_FILE;

    static {
        try {
            // Register JDBC driver
            Class.forName("org.sqlite.JDBC");
            System.out.println("SQLite JDBC Driver registered successfully");
            initializeDatabase();
        } catch (ClassNotFoundException e) {
            System.err.println("Failed to load SQLite JDBC driver: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static void initializeDatabase() {
        System.out.println("Starting database initialization...");
        System.out.println("Database file location: " + new File(DB_FILE).getAbsolutePath());

        try (Connection conn = getConnection()) {
            System.out.println("Database connection established");

            try (Statement stmt = conn.createStatement()) {
                // Create tables
                System.out.println("Creating users table...");
                stmt.execute("""
                    CREATE TABLE IF NOT EXISTS users (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        username TEXT UNIQUE NOT NULL,
                        password TEXT NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                    )
                """);
                System.out.println("Users table created successfully");

                System.out.println("Creating reviews table...");
                stmt.execute("""
                    CREATE TABLE IF NOT EXISTS reviews (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        user_id INTEGER NOT NULL,
                        cafe_name TEXT NOT NULL,
                        rating INTEGER NOT NULL CHECK (rating BETWEEN 1 AND 5),
                        review_text TEXT NOT NULL,
                        latitude DOUBLE NOT NULL,
                        longitude DOUBLE NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (user_id) REFERENCES users(id)
                    )
                """);
                System.out.println("Reviews table created successfully");

                // Verify tables were created
                try (ResultSet rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table'")) {
                    System.out.println("Existing tables in database:");
                    while (rs.next()) {
                        System.out.println(" - " + rs.getString("name"));
                    }
                }
            }
            System.out.println("Database initialization completed successfully");
        } catch (SQLException e) {
            System.err.println("Database initialization failed: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            System.err.println("Failed to connect to database: " + e.getMessage());
            throw e;
        }
    }
}