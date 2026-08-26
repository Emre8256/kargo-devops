package com.emre.kargo.auth.mapper;

import com.emre.kargo.auth.dto.AuthResponse;
import com.emre.kargo.auth.dto.RegisterRequest;
import com.emre.kargo.user.dto.user.UserResponse;
import com.emre.kargo.user.entity.User;
import com.emre.kargo.user.enums.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    // DTO --> ENTITY
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    User toUser(RegisterRequest request);

    // ENTITY --> DTO
    UserResponse toUserResponse(User user);

    // TOKEN --> DTO
    @Mapping(target = "token", source = "token")
    @Mapping(target = "role", source = "role")
    AuthResponse toAuthResponse(String token, Role role);
}
