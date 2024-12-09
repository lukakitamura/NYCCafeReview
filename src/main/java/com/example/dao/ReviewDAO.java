package com.example.dao;

import com.example.model.Review;
import com.example.db.DatabaseManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {
    public void addReview(Review review) throws SQLException {
        String sql = "INSERT INTO reviews (user_id, cafe_name, rating, review_text) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, review.getUserId());
            pstmt.setString(2, review.getCafeName());
            pstmt.setInt(3, review.getRating());
            pstmt.setString(4, review.getReviewText());
            pstmt.executeUpdate();
        }
    }

    public List<Review> getAllReviews() throws SQLException {
        List<Review> reviews = new ArrayList<>();
        String sql = """
            SELECT r.*, u.username 
            FROM reviews r 
            JOIN users u ON r.user_id = u.id 
            ORDER BY r.created_at DESC
        """;

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Review review = new Review();
                review.setId(rs.getInt("id"));
                review.setUserId(rs.getInt("user_id"));
                review.setCafeName(rs.getString("cafe_name"));
                review.setRating(rs.getInt("rating"));
                review.setReviewText(rs.getString("review_text"));
                review.setUsername(rs.getString("username"));
                review.setCreatedAt(rs.getTimestamp("created_at").toInstant());
                reviews.add(review);
            }
        }
        return reviews;
    }
}