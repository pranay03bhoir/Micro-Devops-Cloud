package com.pranay.easybuy.users.config;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.pranay.easybuy.users.dto.UserDto;
import com.pranay.easybuy.users.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserDto userDto);

    @Mapping(target = "password", ignore = true)
    UserDto toDto(User user);

    @Mapping(target = "password", ignore = true)
    List<UserDto> toDtoList(List<User> users);


}
