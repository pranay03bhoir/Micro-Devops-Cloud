package com.substring.blogapp.service.serviceImpl;

import com.substring.blogapp.dto.UserDto;
import com.substring.blogapp.exceptions.AlreadyExistsException;
import com.substring.blogapp.models.Role;
import com.substring.blogapp.models.User;
import com.substring.blogapp.repositories.UserRepository;
import com.substring.blogapp.service.UserRegisterService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRegisterServiceImpl implements UserRegisterService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override
    public UserDto registerUser(UserDto userDto) {

        User user = modelMapper.map(userDto, User.class);
        validateUser(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.ROLE_USER);
        user.setEnabled(true);
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDto.class);
    }

    private void validateUser(User user) {
        User existingUser = userRepository.findByEmail(user.getEmail()).orElse(null);
        if (existingUser != null) {
            throw new AlreadyExistsException("User with " + user.getEmail() + " already exists");
        }
    }
}
