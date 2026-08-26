package com.emre.kargo.user.mapper;

import com.emre.kargo.user.dto.address.AddressRequest;
import com.emre.kargo.user.dto.address.AddressResponse;
import com.emre.kargo.user.dto.address.AddressUpdateRequest;
import com.emre.kargo.user.entity.Address;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    // ENTITY --> DTO
    List<AddressResponse> toAddressResponseList(List<Address> addresses);

    AddressResponse toAddressResponse(Address address);

    // DTO --> ENTITY
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    Address toAddress(AddressRequest request);

    // PATCH
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAddressFromRequest(AddressUpdateRequest request, @MappingTarget Address address);
}
