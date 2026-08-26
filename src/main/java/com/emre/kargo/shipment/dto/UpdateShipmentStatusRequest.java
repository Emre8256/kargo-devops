package com.emre.kargo.shipment.dto;

import com.emre.kargo.shipment.enums.ShipmentStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateShipmentStatusRequest(
        @NotNull(message = "Shipment status is required.")
        ShipmentStatus status
) {
}
