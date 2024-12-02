// CafeDAO.java
package com.cafereview.dao;

import com.cafereview.config.DatabaseConfig;
import com.cafereview.model.Cafe;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CafeDAO {
    
    public List<Cafe> findAll() throws SQLException {
        List<Cafe> cafes = new ArrayList<>();
        String sql = "SELECT * FROM cafes ORDER BY name";
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                cafes.add(mapResultSetToCafe(rs));
            }
        }
        return cafes;
    }
    
    public Cafe findById(int cafeId) throws SQLException {
        String sql = "SELECT * FROM cafes WHERE cafe_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cafeId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCafe(rs);
                }
            }
        }
        return null;
    }
    
    public boolean create(Cafe cafe) throws SQLException {
        String sql = "INSERT INTO cafes (name, street_address, city, state, postal_code, phone, wifi_available) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, cafe.getName());
            stmt.setString(2, cafe.getAddress());
            stmt.setString(3, cafe.getPhone());
            stmt.setBoolean(4, cafe.isWifiAvailable());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        cafe.setCafeId(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    private Cafe mapResultSetToCafe(ResultSet rs) throws SQLException {
        Cafe cafe = new Cafe();
        cafe.setCafeId(rs.getInt("cafe_id"));
        cafe.setName(rs.getString("name"));
        cafe.setAddress(rs.getString("address"));
        cafe.setPhone(rs.getString("phone"));
        cafe.setWifiAvailable(rs.getBoolean("wifi_available"));
        return cafe;
    }
}
