package com.emre.kargo.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank(message = "Email cannot be blank.")
        @Email(message = "Please enter a valid email address.")
        @Size(max = 100, message = "Email can be at most 100 characters.")
        String email,

        @NotBlank(message = "Password cannot be blank.")
        String password
) {
}
