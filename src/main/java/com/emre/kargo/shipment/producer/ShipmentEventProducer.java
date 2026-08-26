package com.emre.kargo.shipment.producer;

import com.emre.kargo.shipment.event.ShipmentStatusChangedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShipmentEventProducer {
    private final KafkaTemplate<String, ShipmentStatusChangedEvent> kafkaTemplate;

    public void sendStatusChangedEvent(ShipmentStatusChangedEvent event) {
        kafkaTemplate.send(
                "shipment-status-changed",
                event.trackingNumber(),
                event
        );
    }
}
