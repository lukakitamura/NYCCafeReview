package com.cafereview.dao;

import com.cafereview.config.DatabaseConfig;
import com.cafereview.model.Review;
import com.cafereview.model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {
    
    public List<Review> findByCafeId(int cafeId) throws SQLException {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT r.*, u.username FROM reviews r " +
                    "JOIN users u ON r.user_id = u.user_id " +
                    "WHERE r.cafe_id = ? " +
                    "ORDER BY r.created_at DESC";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cafeId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    reviews.add(mapResultSetToReview(rs));
                }
            }
        }
        return reviews;
    }
    
    public boolean create(Review review) throws SQLException {
        String sql = "INSERT INTO reviews (cafe_id, user_id, rating, comment) " +
                    "VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, review.getCafeId());
            stmt.setInt(2, review.getUserId());
            stmt.setInt(3, review.getRating());
            stmt.setString(4, review.getComment());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        review.setReviewId(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public double getAverageRatings(int cafeId) throws SQLException {
        String sql = "SELECT AVG(rating) as avg_rating FROM reviews WHERE cafe_id = ? ";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cafeId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
			return rs.getDoublt("avg_rating"); 
                    };
                }
            }
        }
        return 0.0;
    }
    
    private Review mapResultSetToReview(ResultSet rs) throws SQLException {
        Review review = new Review();
        review.setReviewId(rs.getInt("review_id"));
        review.setCafeId(rs.getInt("cafe_id"));
        review.setUserId(rs.getInt("user_id"));
        review.setRestroomRating(rs.getInt("rating"));
        review.setComment(rs.getString("comment"));
        review.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        
        // Set basic user info from join
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email")); 
	review.setUser(user);
        
        return review;
    }
}
