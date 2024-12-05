package com.example.cafereview.dto.request;

public record LoginRequest(
        String username,
        String password
) {}