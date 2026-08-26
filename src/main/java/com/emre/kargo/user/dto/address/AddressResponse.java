package com.emre.kargo.user.dto.address;

public record AddressResponse(
        Long id,
        String title,
        String city,
        String district,
        String fullAddress
) {
}
