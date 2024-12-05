package com.example.cafereview.dto.response;

import com.example.cafereview.model.Review;
import java.time.LocalDateTime;

public record ReviewResponse(
        Long id,
        String username,
        int rating,
        String comment,
        LocalDateTime createdAt
) {
    public static ReviewResponse fromEntity(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getUser().getUsername(),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt()
        );
    }
}