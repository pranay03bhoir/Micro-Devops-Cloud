package com.pranay.easybuy.users.config;

import com.pranay.easybuy.users.dto.UserDto;
import com.pranay.easybuy.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Mapper(componentModel = "spring")
@RequiredArgsConstructor
public abstract class UserMapper {

    protected PasswordEncoder passwordEncoder;

    @Mapping(target = "password", expression = "java(passwordEncoder.encode(userDto.getPassword()))")
    public abstract User toEntity(UserDto userDto);

    @Mapping(target = "password", ignore = true)
    public abstract UserDto toDto(User user);

    public abstract List<UserDto> toDtoList(List<User> users);


}
