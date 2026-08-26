package com.emre.kargo.notification.consumer;

import com.emre.kargo.notification.service.MailService;
import com.emre.kargo.shipment.event.ShipmentStatusChangedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShipmentNotificationConsumer {

    private final MailService mailService;

    @KafkaListener(topics = "shipment-status-changed")
    public void consume(ShipmentStatusChangedEvent event) {

        mailService.sendMail(
                "42oyunus42@gmail.com",
                "Kargo durumu güncellendi",
                "Takip numarası: " + event.trackingNumber()
                        + "\nYeni durum: " + event.newStatus()
        );
    }
}
