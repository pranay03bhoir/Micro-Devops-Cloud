package com.pranay.easybuy.users.services;

import com.pranay.easybuy.users.dto.LoginRequest;
import com.pranay.easybuy.users.dto.LoginResponse;
import com.pranay.easybuy.users.dto.UserDto;
import com.pranay.easybuy.users.entity.Role;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto getUserById(UUID id);

    UserDto getUserByEmail(String email);

    List<UserDto> getAllUsers();

    UserDto updateUser(UUID id, UserDto userDto);

    UserDto deleteUser(UUID id);

    void changeUserRole(UUID id, Role role);

    LoginResponse login(LoginRequest loginRequest);
}
