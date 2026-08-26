package com.emre.kargo.user.service;

import com.emre.kargo.user.dto.user.UserResponse;
import com.emre.kargo.user.dto.user.UserUpdateRequest;

import java.util.List;

public interface UserService {

    List<UserResponse> getAllUsers();

    UserResponse getOneUser(Long id);

    void deleteUser(Long id);

    UserResponse updateUser(Long id, UserUpdateRequest updateRequest);
}
