package com.emre.kargo.shipment.controller;

import com.emre.kargo.shipment.dto.CreateShipmentRequest;
import com.emre.kargo.shipment.dto.ShipmentResponse;
import com.emre.kargo.shipment.dto.ShipmentTrackingResponse;
import com.emre.kargo.shipment.dto.UpdateShipmentStatusRequest;
import com.emre.kargo.shipment.service.ShipmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/shipments")
@RequiredArgsConstructor
public class ShipmentController {

    private final ShipmentService shipmentService;

    @GetMapping
    public ResponseEntity<List<ShipmentResponse>> getAllShipments() {
        return ResponseEntity.ok(shipmentService.getAllShipments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShipmentResponse> getOneShipment(@PathVariable Long id) {
        return ResponseEntity.ok(shipmentService.getOneShipment(id));
    }

    @GetMapping("/tracking/{trackingNumber}")
    public ResponseEntity<ShipmentTrackingResponse> trackShipment(@PathVariable String trackingNumber) {
        return ResponseEntity.ok(shipmentService.trackShipment(trackingNumber));
    }

    @PostMapping
    public ResponseEntity<ShipmentResponse> createShipment(@Valid @RequestBody CreateShipmentRequest request) {
        ShipmentResponse response = shipmentService.createShipment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ShipmentResponse> updateShipmentStatus(@PathVariable Long id, @Valid @RequestBody UpdateShipmentStatusRequest request) {
        return ResponseEntity.ok(shipmentService.updateShipmentStatus(id, request));
    }
}
