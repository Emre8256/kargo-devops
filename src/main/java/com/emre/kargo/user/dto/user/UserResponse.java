package com.emre.kargo.user.dto.user;

import com.emre.kargo.user.enums.Role;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phone,
        Role role,
        LocalDateTime createdAt
) {
}
