package com.cafereview.config;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import com.mysql.jdbc.Driver;

public class DatabaseConfig {
    private static final String CONFIG_FILE = "db.properties";
    private static Properties props;
    
    static {
        try {
            // Load properties file
            props = new Properties();
            props.load(DatabaseConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE));
            
            // Register JDBC driver
            Class.forName(props.getProperty("jdbc.driver"));
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to load database configuration", e);
        }
    }
    
    public static Connection getConnection() throws SQLException {
        return java.sql.DriverManager.getConnection(
            props.getProperty("jdbc.url"),
            props.getProperty("jdbc.username"),
            props.getProperty("jdbc.password")
        );
    }
}
