package com.example.cafereview.dto.request;

public record RegisterRequest(
        String username,
        String password,
        String email
) {}
