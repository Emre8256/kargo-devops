package com.emre.kargo.auth.dto;
import com.emre.kargo.user.enums.Role;

public record AuthResponse(
        String token,
        Role role
) {
}
