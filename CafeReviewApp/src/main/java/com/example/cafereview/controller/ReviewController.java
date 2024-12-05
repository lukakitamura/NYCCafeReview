package com.example.cafereview.controller;

import com.example.cafereview.dto.request.ReviewRequest;
import com.example.cafereview.dto.response.ReviewResponse;
import com.example.cafereview.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cafes/{cafeId}/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public List<ReviewResponse> getCafeReviews(@PathVariable Long cafeId) {
        return reviewService.findByCafeId(cafeId).stream()
                .map(ReviewResponse::fromEntity)
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewResponse createReview(
            @PathVariable Long cafeId,
            @RequestBody ReviewRequest request,
            Authentication authentication) {
        return ReviewResponse.fromEntity(
                reviewService.create(cafeId, request, authentication.getName())
        );
    }

    @DeleteMapping("/{reviewId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReview(
            @PathVariable Long cafeId,
            @PathVariable Long reviewId,
            Authentication authentication) {
        reviewService.delete(reviewId, authentication.getName());
    }
}