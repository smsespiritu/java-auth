package com.replit.authapi.dto;

import java.util.Set;

public record AuthResponse(
        String accessToken,
        String tokenType,
        long expiresIn,
        String email,
        Set<String> roles
) {
}