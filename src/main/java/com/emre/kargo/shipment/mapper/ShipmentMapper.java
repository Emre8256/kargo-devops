package com.emre.kargo.shipment.mapper;

import com.emre.kargo.shipment.dto.CreateShipmentRequest;
import com.emre.kargo.shipment.dto.ShipmentTrackingResponse;
import com.emre.kargo.shipment.entity.Shipment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ShipmentMapper {

    // ENTITY --> DTO
    ShipmentTrackingResponse toShipmentTrackingResponse(Shipment shipment);

    // DTO --> ENTITY
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "trackingNumber", ignore = true)
    @Mapping(target = "senderUserId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Shipment toShipment(CreateShipmentRequest request);
}
