package com.example.cafereview.dto.request;

public record ReviewRequest(
        int rating,
        String comment
) {}