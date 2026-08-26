package com.emre.kargo.user.mapper;

import com.emre.kargo.user.dto.user.UserResponse;
import com.emre.kargo.user.dto.user.UserUpdateRequest;
import com.emre.kargo.user.entity.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // ENTITY --> DTO
    UserResponse toUserResponse(User user);
    List<UserResponse> toUserResponseList(List<User> users);

    //PATCH
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromRequest(UserUpdateRequest request, @MappingTarget User user);

}
