package com.example.cafereview.dto.response;

import com.example.cafereview.model.Cafe;
import java.util.List;

public record CafeDetailResponse(
        Long id,
        String name,
        String address,
        boolean hasWifi,
        boolean hasRestroom,
        int seatingCapacity,
        double averageRating,
        String creatorUsername,
        List<ReviewResponse> reviews
) {
    public static CafeDetailResponse fromEntity(Cafe cafe) {
        return new CafeDetailResponse(
                cafe.getId(),
                cafe.getName(),
                cafe.getAddress(),
                cafe.isHasWifi(),
                cafe.isHasRestroom(),
                cafe.getSeatingCapacity(),
                cafe.getAverageRating(),
                cafe.getCreator().getUsername(),
                cafe.getReviews().stream()
                        .map(ReviewResponse::fromEntity)
                        .toList()
        );
    }
}