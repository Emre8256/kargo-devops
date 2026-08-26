package com.emre.kargo.shipment.dto;

import com.emre.kargo.shipment.enums.ShipmentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ShipmentResponse(
        Long id,
        String trackingNumber,
        Long senderUserId,
        Long receiverUserId,
        Long senderAddressId,
        Long receiverAddressId,
        String senderName,
        String receiverName,
        String senderAddress,
        String receiverAddress,
        BigDecimal weight,
        String description,
        ShipmentStatus status,
        LocalDateTime createdAt
) {
}
