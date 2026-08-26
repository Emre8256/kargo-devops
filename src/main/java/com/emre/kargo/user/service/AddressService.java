package com.emre.kargo.user.service;

import com.emre.kargo.user.dto.address.AddressRequest;
import com.emre.kargo.user.dto.address.AddressResponse;
import com.emre.kargo.user.dto.address.AddressUpdateRequest;

import java.util.List;

public interface AddressService {

    List<AddressResponse> getAllAddresses();

    List<AddressResponse> getAddressesByUserId(Long userId);

    AddressResponse getOneAddress(Long id);

    AddressResponse createAddress(AddressRequest request);

    AddressResponse updateAddress(Long id, AddressUpdateRequest request);

    void deleteAddress(Long id);
}
