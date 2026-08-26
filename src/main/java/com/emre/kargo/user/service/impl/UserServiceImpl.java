package com.emre.kargo.user.service.impl;

import com.emre.kargo.common.exception.BadRequestException;
import com.emre.kargo.common.exception.ConflictException;
import com.emre.kargo.common.exception.NotFoundException;
import com.emre.kargo.user.dto.user.UserResponse;
import com.emre.kargo.user.dto.user.UserUpdateRequest;
import com.emre.kargo.user.entity.User;
import com.emre.kargo.user.mapper.UserMapper;
import com.emre.kargo.user.repository.UserRepository;
import com.emre.kargo.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return userMapper.toUserResponseList(users);
    }

    @Override
    public UserResponse getOneUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("User not found.")
                );
        return userMapper.toUserResponse(user);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("User not found.")
                );
        userRepository.delete(user);
    }

    @Override
    public UserResponse updateUser(Long id, UserUpdateRequest updateRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("User not found.")
                );

        if (updateRequest.password() != null) {
            throw new BadRequestException("Password cannot be updated from user management endpoint.");
        }

        if (updateRequest.email() != null && userRepository.existsByEmailAndIdNot(updateRequest.email(), id)) {
            throw new ConflictException("Email address is already in use.");
        }
        if (updateRequest.phone() != null && userRepository.existsByPhoneAndIdNot(updateRequest.phone(), id)) {
            throw new ConflictException("Phone number is already in use.");
        }

        userMapper.updateUserFromRequest(updateRequest, user);

        User updatedUser = userRepository.save(user);
        return userMapper.toUserResponse(updatedUser);
    }
}
