package com.emre.kargo.user.controller;

import com.emre.kargo.user.dto.address.AddressRequest;
import com.emre.kargo.user.dto.address.AddressResponse;
import com.emre.kargo.user.dto.address.AddressUpdateRequest;
import com.emre.kargo.user.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping
    public ResponseEntity<List<AddressResponse>> getAllAddresses(){
        return ResponseEntity.ok(addressService.getAllAddresses());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AddressResponse>> getAddressesByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(addressService.getAddressesByUserId(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressResponse> getOneAddress(@PathVariable Long id){
        return ResponseEntity.ok(addressService.getOneAddress(id));
    }

    @PostMapping
    public ResponseEntity<AddressResponse> createAddress(@Valid @RequestBody AddressRequest addressRequest){
        AddressResponse response = addressService.createAddress(addressRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AddressResponse> updateAddress(@PathVariable Long id, @Valid @RequestBody AddressUpdateRequest request) {
        return ResponseEntity.ok(addressService.updateAddress(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id){
        addressService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }
}
