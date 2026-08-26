package com.emre.kargo.shipment.service;

import com.emre.kargo.shipment.dto.CreateShipmentRequest;
import com.emre.kargo.shipment.dto.ShipmentResponse;
import com.emre.kargo.shipment.dto.ShipmentTrackingResponse;
import com.emre.kargo.shipment.dto.UpdateShipmentStatusRequest;

import java.util.List;

public interface ShipmentService {

    List<ShipmentResponse> getAllShipments();

    ShipmentResponse getOneShipment(Long id);

    ShipmentTrackingResponse trackShipment(String trackingNumber);

    ShipmentResponse createShipment(CreateShipmentRequest request);

    ShipmentResponse updateShipmentStatus(Long id, UpdateShipmentStatusRequest request);
}
