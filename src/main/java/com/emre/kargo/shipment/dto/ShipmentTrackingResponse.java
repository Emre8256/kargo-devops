package com.emre.kargo.shipment.dto;

import com.emre.kargo.shipment.enums.ShipmentStatus;

import java.time.LocalDateTime;

public record ShipmentTrackingResponse(
        ShipmentStatus status,
        String description,
        LocalDateTime createdAt
) {
}
