package com.emre.kargo.user.service.impl;

import com.emre.kargo.common.exception.NotFoundException;
import com.emre.kargo.user.dto.address.AddressRequest;
import com.emre.kargo.user.dto.address.AddressResponse;
import com.emre.kargo.user.dto.address.AddressUpdateRequest;
import com.emre.kargo.user.entity.Address;
import com.emre.kargo.user.entity.User;
import com.emre.kargo.user.mapper.AddressMapper;
import com.emre.kargo.user.repository.AddressRepository;
import com.emre.kargo.user.repository.UserRepository;
import com.emre.kargo.user.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final UserRepository userRepository;

    @Override
    public List<AddressResponse> getAllAddresses() {
        User currentUser = getCurrentUser();
        List<Address> addresses = addressRepository.findByUserId(currentUser.getId());
        return addressMapper.toAddressResponseList(addresses);
    }

    @Override
    public List<AddressResponse> getAddressesByUserId(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() ->
                        new NotFoundException("User not found.")
                );

        List<Address> addresses = addressRepository.findByUserId(userId);

        return addressMapper.toAddressResponseList(addresses);
    }

    @Override
    public AddressResponse getOneAddress(Long id) {
        User currentUser = getCurrentUser();
        Address address = addressRepository
                .findByIdAndUserId(id, currentUser.getId())
                .orElseThrow(() ->
                        new NotFoundException("Address not found.")
                );
        return addressMapper.toAddressResponse(address);
    }

    @Override
    public AddressResponse createAddress(AddressRequest request) {
        User currentUser = getCurrentUser();

        Address address = addressMapper.toAddress(request);
        address.setUser(currentUser);

        Address savedAddress = addressRepository.save(address);

        return addressMapper.toAddressResponse(savedAddress);
    }

    @Override
    public AddressResponse updateAddress(Long id, AddressUpdateRequest request) {
        User currentUser = getCurrentUser();

        Address address = addressRepository
                .findByIdAndUserId(id, currentUser.getId())
                .orElseThrow(() ->
                        new NotFoundException("Address not found.")
                );

        addressMapper.updateAddressFromRequest(request, address);
        Address updatedAddress = addressRepository.save(address);
        return addressMapper.toAddressResponse(updatedAddress);
    }

    @Override
    public void deleteAddress(Long id) {
        User currentUser = getCurrentUser();
        Address address = addressRepository
                .findByIdAndUserId(id, currentUser.getId())
                .orElseThrow(() ->
                        new NotFoundException("Address not found.")
                );
        addressRepository.delete(address);
    }

    private User getCurrentUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new NotFoundException("Authenticated user not found.")
                );
    }
}
