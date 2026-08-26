package com.emre.kargo.auth.service;

import com.emre.kargo.auth.dto.AuthResponse;
import com.emre.kargo.auth.dto.LoginRequest;
import com.emre.kargo.auth.dto.RegisterRequest;
import com.emre.kargo.user.dto.user.UserResponse;

public interface AuthService {

    AuthResponse login(LoginRequest request);

    UserResponse register(RegisterRequest request);
}
