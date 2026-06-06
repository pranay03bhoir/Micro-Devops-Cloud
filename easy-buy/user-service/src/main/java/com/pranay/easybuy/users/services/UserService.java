package com.pranay.easybuy.users.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.pranay.easybuy.users.dto.UserDto;
import com.pranay.easybuy.users.entity.Role;

@Service
public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto getUserById(UUID id);

    UserDto getUserByEmail(String email);

    List<UserDto> getAllUsers();

    UserDto updateUser(UUID id, UserDto userDto);

    UserDto deleteUser(UUID id);

    void changeUserRole(UUID id, Role role);
}
