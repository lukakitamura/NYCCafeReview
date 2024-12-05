package com.example.cafereview.service;

import com.example.cafereview.model.Review;
import com.example.cafereview.model.Cafe;
import com.example.cafereview.model.User;
import com.example.cafereview.dto.response.ReviewResponse;
import com.example.cafereview.repository.ReviewRepository;
import com.example.cafereview.repository.CafeRepository;
import com.example.cafereview.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final CafeRepository cafeRepository;
    private final UserRepository userRepository;

    public ReviewService(ReviewRepository reviewRepository,
                         CafeRepository cafeRepository,
                         UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.cafeRepository = cafeRepository;
        this.userRepository = userRepository;
    }

    public List<ReviewResponse> getCafeReviews(Long cafeId) {
        return reviewRepository.findByCafeId(cafeId).stream()
                .map(ReviewResponse::fromEntity)
                .toList();
    }

    public ReviewResponse createReview(Long cafeId, String username, int rating, String comment) {
        if (rating < 1 || rating > 5) {
            throw new RuntimeException("Rating must be between 1 and 5");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(() -> new RuntimeException("Cafe not found"));

        if (reviewRepository.existsByUserUsernameAndCafeId(username, cafeId)) {
            throw new RuntimeException("User has already reviewed this cafe");
        }

        Review review = new Review();
        review.setUser(user);
        review.setCafe(cafe);
        review.setRating(rating);
        review.setComment(comment);

        Review savedReview = reviewRepository.save(review);
        cafe.updateAverageRating();
        cafeRepository.save(cafe);

        return ReviewResponse.fromEntity(savedReview);
    }

    public void deleteReview(Long reviewId, String username) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        if (!review.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Not authorized to delete this review");
        }

        reviewRepository.delete(review);
        Cafe cafe = review.getCafe();
        cafe.updateAverageRating();
        cafeRepository.save(cafe);
    }
}