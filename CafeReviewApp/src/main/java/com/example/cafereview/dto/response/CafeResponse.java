package com.example.cafereview.dto.response;

import com.example.cafereview.model.Cafe;

public record CafeResponse(
        Long id,
        String name,
        String address,
        double averageRating,
        int numberOfReviews
) {
    public static CafeResponse fromEntity(Cafe cafe) {
        return new CafeResponse(
                cafe.getId(),
                cafe.getName(),
                cafe.getAddress(),
                cafe.getAverageRating(),
                cafe.getReviews().size()
        );
    }
}