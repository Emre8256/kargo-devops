package com.emre.kargo.user.dto.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddressRequest(
        @NotBlank(message = "Address title cannot be blank.")
        @Size(max = 30, message = "Address title can be at most 30 characters.")
        String title,

        @NotBlank(message = "City cannot be blank.")
        @Size(max = 30, message = "City can be at most 30 characters.")
        String city,

        @NotBlank(message = "District cannot be blank.")
        @Size(max = 30, message = "District can be at most 30 characters.")
        String district,

        @NotBlank(message = "Full address cannot be blank.")
        String fullAddress
) {
}
