package com.emre.kargo.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "First name cannot be blank.")
        @Size(max = 50, message = "First name can be at most 50 characters.")
        String firstName,

        @NotBlank(message = "Last name cannot be blank.")
        @Size(max = 50, message = "Last name can be at most 50 characters.")
        String lastName,

        @NotBlank(message = "Email cannot be blank.")
        @Email(message = "Please enter a valid email address.")
        @Size(max = 100, message = "Email can be at most 100 characters.")
        String email,

        @NotBlank(message = "Password cannot be blank.")
        @Size(
                min = 8,
                max = 50,
                message = "Password must be between 8 and 50 characters."
        )
        String password,

        @NotBlank(message = "Phone cannot be blank.")
        @Pattern(
                regexp = "^5\\d{9}$",
                message = "Phone must be 10 digits and must not start with 0."
        )
        String phone
) {
}
