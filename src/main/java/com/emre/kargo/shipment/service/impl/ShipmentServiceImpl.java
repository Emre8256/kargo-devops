package com.emre.kargo.shipment.service.impl;

import com.emre.kargo.common.exception.ForbiddenException;
import com.emre.kargo.common.exception.NotFoundException;
import com.emre.kargo.shipment.dto.CreateShipmentRequest;
import com.emre.kargo.shipment.dto.ShipmentResponse;
import com.emre.kargo.shipment.dto.ShipmentTrackingResponse;
import com.emre.kargo.shipment.dto.UpdateShipmentStatusRequest;
import com.emre.kargo.shipment.entity.Shipment;
import com.emre.kargo.shipment.enums.ShipmentStatus;
import com.emre.kargo.shipment.event.ShipmentStatusChangedEvent;
import com.emre.kargo.shipment.mapper.ShipmentMapper;
import com.emre.kargo.shipment.producer.ShipmentEventProducer;
import com.emre.kargo.shipment.repository.ShipmentRepository;
import com.emre.kargo.shipment.service.ShipmentService;
import com.emre.kargo.user.entity.User;
import com.emre.kargo.user.enums.Role;
import com.emre.kargo.user.repository.AddressRepository;
import com.emre.kargo.user.repository.UserRepository;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShipmentServiceImpl implements ShipmentService {

    private static final String TRACKING_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int TRACKING_NUMBER_LENGTH = 10;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private static final String TRACKING_CACHE_PREFIX = "shipment:tracking:";
    private static final Duration TRACKING_CACHE_TTL = Duration.ofMinutes(5);

    private final ShipmentRepository shipmentRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final ShipmentMapper shipmentMapper;
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    private final ShipmentEventProducer shipmentEventProducer;

    @Override
    public List<ShipmentResponse> getAllShipments() {
        User currentUser = getCurrentUser();

        List<Shipment> shipments;

        if (isAdmin(currentUser)) {
            shipments = shipmentRepository.findAll();
        } else {
            shipments = shipmentRepository.findBySenderUserIdOrReceiverUserId(currentUser.getId(), currentUser.getId());
        }

        return shipments.stream()
                .map(this::toShipmentResponse)
                .toList();
    }

    @Override
    public ShipmentResponse getOneShipment(Long id) {
        User currentUser = getCurrentUser();
        Shipment shipment = findVisibleShipment(id, currentUser);

        return toShipmentResponse(shipment);
    }

    @Override
    public ShipmentTrackingResponse trackShipment(String trackingNumber) {
        String cacheKey = buildTrackingCacheKey(trackingNumber);
        String cachedShipment = redisTemplate.opsForValue().get(cacheKey);

        if (cachedShipment != null) {
            try {
                return objectMapper.readValue(cachedShipment, ShipmentTrackingResponse.class);
            } catch (JacksonException exception) {
                log.warn("Cached shipment tracking response could not be read. trackingNumber={}", trackingNumber, exception);
                redisTemplate.delete(cacheKey);
            }
        }

        Shipment shipment = shipmentRepository.findByTrackingNumber(trackingNumber)
                .orElseThrow(() -> new NotFoundException("Shipment not found."));

        ShipmentTrackingResponse response = shipmentMapper.toShipmentTrackingResponse(shipment);

        try {
            String responseJson = objectMapper.writeValueAsString(response);
            redisTemplate.opsForValue().set(cacheKey, responseJson, TRACKING_CACHE_TTL);
        } catch (JacksonException exception) {
            log.warn("Shipment tracking response could not be cached. trackingNumber={}", trackingNumber, exception);
        }

        return response;
    }

    @Override
    public ShipmentResponse createShipment(CreateShipmentRequest request) {
        User currentUser = getCurrentUser();
        if (!isAdmin(currentUser)) {
            throw new ForbiddenException("Only admins can create shipments.");
        }

        userRepository.findById(request.senderUserId())
                .orElseThrow(() -> new NotFoundException("Sender user not found."));

        userRepository.findById(request.receiverUserId())
                .orElseThrow(() -> new NotFoundException("Receiver user not found."));

        addressRepository.findByIdAndUserId(request.senderAddressId(), request.senderUserId())
                .orElseThrow(() -> new NotFoundException("Sender address not found."));

        addressRepository.findByIdAndUserId(request.receiverAddressId(), request.receiverUserId())
                .orElseThrow(() -> new NotFoundException("Receiver address not found."));

        Shipment shipment = shipmentMapper.toShipment(request);
        shipment.setTrackingNumber(generateUniqueTrackingNumber());
        shipment.setSenderUserId(request.senderUserId());
        shipment.setStatus(ShipmentStatus.CREATED);

        Shipment savedShipment = shipmentRepository.save(shipment);

        return toShipmentResponse(savedShipment);
    }

    @Override
    public ShipmentResponse updateShipmentStatus(Long id, UpdateShipmentStatusRequest request) {
        User currentUser = getCurrentUser();

        if (!isAdmin(currentUser)) {
            throw new ForbiddenException("Only admins can update shipment status.");
        }

        Shipment shipment = shipmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Shipment not found."));

        shipment.setStatus(request.status());
        Shipment updatedShipment = shipmentRepository.save(shipment);

        ShipmentStatusChangedEvent event =
                new ShipmentStatusChangedEvent(
                        updatedShipment.getTrackingNumber(),
                        updatedShipment.getStatus(),
                        updatedShipment.getSenderUserId(),
                        updatedShipment.getReceiverUserId(),
                        LocalDateTime.now()
                );

        shipmentEventProducer.sendStatusChangedEvent(event);

        String cacheKey = buildTrackingCacheKey(updatedShipment.getTrackingNumber());
        ShipmentTrackingResponse response = shipmentMapper.toShipmentTrackingResponse(updatedShipment);

        try {
            String responseJson = objectMapper.writeValueAsString(response);
            redisTemplate.opsForValue().set(cacheKey, responseJson, TRACKING_CACHE_TTL);
        } catch (RuntimeException exception) {
            log.warn("Shipment tracking cache could not be refreshed. trackingNumber={}", updatedShipment.getTrackingNumber(), exception);
        }

        return toShipmentResponse(updatedShipment);
    }

    private Shipment findVisibleShipment(Long id, User currentUser) {
        if (isAdmin(currentUser)) {
            return shipmentRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Shipment not found."));
        }

        return shipmentRepository.findByIdAndSenderUserIdOrIdAndReceiverUserId(id, currentUser.getId(), id, currentUser.getId()
        ).orElseThrow(() -> new NotFoundException("Shipment not found."));
    }

    private String generateUniqueTrackingNumber() {
        String trackingNumber;
        do {
            trackingNumber = generateTrackingNumber();
        } while (shipmentRepository.existsByTrackingNumber(trackingNumber));

        return trackingNumber;
    }

    private String generateTrackingNumber() {
        StringBuilder builder = new StringBuilder(TRACKING_NUMBER_LENGTH);
        for (int i = 0; i < TRACKING_NUMBER_LENGTH; i++) {
            int index = SECURE_RANDOM.nextInt(TRACKING_ALPHABET.length());
            builder.append(TRACKING_ALPHABET.charAt(index));
        }
        return builder.toString();
    }

    private User getCurrentUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Authenticated user not found."));
    }

    private boolean isAdmin(User user) {
        return Role.ADMIN.equals(user.getRole());
    }

    private ShipmentResponse toShipmentResponse(Shipment shipment) {
        return new ShipmentResponse(
                shipment.getId(),
                shipment.getTrackingNumber(),
                shipment.getSenderUserId(),
                shipment.getReceiverUserId(),
                shipment.getSenderAddressId(),
                shipment.getReceiverAddressId(),
                userSummary(shipment.getSenderUserId()),
                userSummary(shipment.getReceiverUserId()),
                addressSummary(shipment.getSenderAddressId(), shipment.getSenderUserId()),
                addressSummary(shipment.getReceiverAddressId(), shipment.getReceiverUserId()),
                shipment.getWeight(),
                shipment.getDescription(),
                shipment.getStatus(),
                shipment.getCreatedAt()
        );
    }

    private String userSummary(Long userId) {
        return userRepository.findById(userId)
                .map(user -> user.getFirstName() + " " + user.getLastName())
                .orElse("Kullanıcı bulunamadı");
    }

    private String addressSummary(Long addressId, Long userId) {
        return addressRepository.findByIdAndUserId(addressId, userId)
                .map(address -> address.getTitle() + " - " + address.getCity() + " / " + address.getDistrict())
                .orElse("Adres bulunamadı");
    }

    private String buildTrackingCacheKey(String trackingNumber) {
        return TRACKING_CACHE_PREFIX + trackingNumber;
    }  

}
