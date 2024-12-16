package com.example.listener;

import com.example.db.DatabaseManager;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class DatabaseInitializationListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Initializing database...");
        try {
            Class.forName("com.example.db.DatabaseManager");
            System.out.println("Database initialization completed");
        } catch (ClassNotFoundException e) {
            System.err.println("Failed to initialize database: " + e.getMessage());
            throw new RuntimeException("Database initialization failed", e);
        }
    }
}