package com.emre.kargo.user.dto.address;

import jakarta.validation.constraints.Size;

public record AddressUpdateRequest(
        @Size(max = 30, message = "Address title can be at most 30 characters.")
        String title,

        @Size(max = 30, message = "City can be at most 30 characters.")
        String city,

        @Size(max = 30, message = "District can be at most 30 characters.")
        String district,

        String fullAddress
) {
}
