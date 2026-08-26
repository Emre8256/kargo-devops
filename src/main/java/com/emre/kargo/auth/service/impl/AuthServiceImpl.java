package com.emre.kargo.auth.service.impl;

import com.emre.kargo.auth.dto.AuthResponse;
import com.emre.kargo.auth.dto.LoginRequest;
import com.emre.kargo.auth.dto.RegisterRequest;
import com.emre.kargo.auth.mapper.AuthMapper;
import com.emre.kargo.auth.service.AuthService;
import com.emre.kargo.common.exception.ConflictException;
import com.emre.kargo.security.CustomUserDetailsService;
import com.emre.kargo.security.JwtService;
import com.emre.kargo.user.dto.user.UserResponse;
import com.emre.kargo.user.entity.User;
import com.emre.kargo.user.enums.Role;
import com.emre.kargo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AuthMapper authMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.email());

        String token = jwtService.generateToken(userDetails);
        Role role = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority -> authority.startsWith("ROLE_"))
                .map(authority -> Role.valueOf(authority.substring("ROLE_".length())))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Authenticated user role could not be found."));

        return authMapper.toAuthResponse(token, role);
    }

    @Override
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new ConflictException("Email address is already in use.");
        }

        if (userRepository.existsByPhone(request.phone())) {
            throw new ConflictException("Phone number is already in use.");
        }

        User user = authMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.CUSTOMER);

        User savedUser = userRepository.save(user);

        return authMapper.toUserResponse(savedUser);
    }
}
