package com.example.bankcards.service;

import com.example.bankcards.dto.UserCreateRequest;
import com.example.bankcards.dto.UserDto;
import com.example.bankcards.dto.UserUpdateRequest;

import java.util.List;

public interface UserService {

    UserDto createUser(UserCreateRequest request);

    UserDto updateUser(Long id, UserUpdateRequest request);

    void deleteUser(Long id);

    UserDto getUserById(Long id);

    List<UserDto> getAllUsers();

    UserDto getCurrentUser();
}
