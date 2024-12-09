package com.example.db;

import java.sql.*;
import java.nio.file.*;

public class DatabaseManager {
    private static final String DB_FILE = "cafe_review.db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_FILE;

    static {
        initializeDatabase();
    }

    private static void initializeDatabase() {
        if (!Files.exists(Path.of(DB_FILE))) {
            try (Connection conn = getConnection()) {
                try (Statement stmt = conn.createStatement()) {
                    // Create tables
                    stmt.execute("""
                        CREATE TABLE IF NOT EXISTS users (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            username TEXT UNIQUE NOT NULL,
                            password TEXT NOT NULL,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                        )
                    """);

                    stmt.execute("""
                        CREATE TABLE IF NOT EXISTS reviews (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            user_id INTEGER NOT NULL,
                            cafe_name TEXT NOT NULL,
                            rating INTEGER NOT NULL CHECK (rating BETWEEN 1 AND 5),
                            review_text TEXT NOT NULL,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            FOREIGN KEY (user_id) REFERENCES users(id)
                        )
                    """);
                }
            } catch (SQLException e) {
                throw new RuntimeException("Failed to initialize database", e);
            }
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
}