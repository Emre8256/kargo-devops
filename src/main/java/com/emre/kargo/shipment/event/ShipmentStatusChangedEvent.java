package com.emre.kargo.shipment.event;

import com.emre.kargo.shipment.enums.ShipmentStatus;

import java.time.LocalDateTime;

public record ShipmentStatusChangedEvent(
        String trackingNumber,
        ShipmentStatus newStatus,
        Long senderUserId,
        Long receiverUserId,
        LocalDateTime changedAt
) {
}