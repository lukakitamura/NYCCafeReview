package com.example.cafereview.dto.request;

public record CafeRequest(
        String name,
        String address,
        boolean hasWifi,
        boolean hasRestroom,
        int seatingCapacity
) {}