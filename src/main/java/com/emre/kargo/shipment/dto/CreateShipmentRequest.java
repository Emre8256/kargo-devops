package com.emre.kargo.shipment.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateShipmentRequest(
        @NotNull(message = "Sender user id is required.")
        Long senderUserId,

        @NotNull(message = "Receiver user id is required.")
        Long receiverUserId,

        @NotNull(message = "Sender address id is required.")
        Long senderAddressId,

        @NotNull(message = "Receiver address id is required.")
        Long receiverAddressId,

        @NotNull(message = "Weight is required.")
        @DecimalMin(value = "0.01", message = "Weight must be greater than zero.")
        @Digits(integer = 8, fraction = 2, message = "Weight format is invalid.")
        BigDecimal weight,

        @Size(max = 500, message = "Description can be at most 500 characters.")
        String description
) {
}
