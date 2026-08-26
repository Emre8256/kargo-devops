package com.emre.kargo.shipment.repository;

import com.emre.kargo.shipment.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShipmentRepository extends JpaRepository<Shipment,Long> {
    Optional<Shipment> findByTrackingNumber(String trackingNumber);
    boolean existsByTrackingNumber(String trackingNumber);
    List<Shipment> findBySenderUserIdOrReceiverUserId(Long senderUserId, Long receiverUserId);
    Optional<Shipment> findByIdAndSenderUserIdOrIdAndReceiverUserId(Long senderShipmentId, Long senderUserId, Long receiverShipmentId, Long receiverUserId);
}
