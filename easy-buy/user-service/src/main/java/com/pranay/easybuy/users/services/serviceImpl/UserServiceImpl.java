package com.pranay.easybuy.users.services.serviceImpl;

import com.pranay.easybuy.users.config.UserMapper;
import com.pranay.easybuy.users.dto.LoginRequest;
import com.pranay.easybuy.users.dto.LoginResponse;
import com.pranay.easybuy.users.dto.UserDto;
import com.pranay.easybuy.users.entity.RefreshToken;
import com.pranay.easybuy.users.entity.Role;
import com.pranay.easybuy.users.entity.User;
import com.pranay.easybuy.users.exceptions.InvalidRequestException;
import com.pranay.easybuy.users.exceptions.ResourceNotFoundException;
import com.pranay.easybuy.users.repository.RefreshTokenRepository;
import com.pranay.easybuy.users.repository.UserRepository;
import com.pranay.easybuy.users.security.services.JwtService;
import com.pranay.easybuy.users.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

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

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        log.info("login service started");

        // Password matching logic using Spring security
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(), loginRequest.getPassword()
            ));
        } catch (AuthenticationException e) {
            throw new InvalidRequestException("Invalid username or password");
        }

        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new InvalidRequestException("Invalid username or password"));

        // Manual password matching logic.
//        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
//            throw new InvalidRequestException("Invalid email or password");
//        }

        String accessToken = jwtService.generateAccessToken(user.getId(), user.getEmail(), user.getRole().name());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());
        RefreshToken refreshTokenObj = new RefreshToken();
        refreshTokenObj.setRefreshToken(refreshToken);
        refreshTokenObj.setActive(true);
        refreshTokenObj.setUser(user);
        refreshTokenRepository.save(refreshTokenObj);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setAccessToken(accessToken);
        loginResponse.setRefreshToken(refreshToken);
        loginResponse.setUser(userMapper.toDto(user));
        log.info("Login service executed:");
        return loginResponse;
    }
}
