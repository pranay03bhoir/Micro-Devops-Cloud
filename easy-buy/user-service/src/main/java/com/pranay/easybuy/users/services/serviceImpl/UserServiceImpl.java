package com.pranay.easybuy.users.services.serviceImpl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.pranay.easybuy.users.config.UserMapper;
import com.pranay.easybuy.users.dto.UserDto;
import com.pranay.easybuy.users.entity.Role;
import com.pranay.easybuy.users.entity.User;
import com.pranay.easybuy.users.exceptions.InvalidRequestException;
import com.pranay.easybuy.users.exceptions.ResourceNotFoundException;
import com.pranay.easybuy.users.repository.UserRepository;
import com.pranay.easybuy.users.services.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto createUser(UserDto userDto) {
        userRepository.findByEmail(userDto.getEmail())
                .ifPresent(u -> {
                    throw new InvalidRequestException("User already exists!!!");
                });
        User createdUser = userMapper.toEntity(userDto);
        User savedUser = userRepository.save(createdUser);
        return userMapper.toDto(savedUser);

    }

    @Override
    public UserDto getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!!!"));
        return userMapper.toDto(user);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not Found!!!"));
        return userMapper.toDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> user = userRepository.findAll();
        return userMapper.toDtoList(user);
    }

    @Override
    public UserDto updateUser(UUID id, UserDto userDto) {
        userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!!!"));
        User updatedUser = userMapper.toEntity(userDto);
        User savedUpdatedUser = userRepository.save(updatedUser);
        return userMapper.toDto(savedUpdatedUser);
    }

    @Override
    public UserDto deleteUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!!!"));
        userRepository.deleteById(id);
        return userMapper.toDto(user);
    }

    @Override
    public void changeUserRole(UUID id, Role role) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!!!"));
        user.setRole(role);
        userRepository.save(user);

    }
}
