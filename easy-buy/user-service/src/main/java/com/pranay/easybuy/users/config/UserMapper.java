package com.pranay.easybuy.users.config;

import java.util.List;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.pranay.easybuy.users.dto.UserDto;
import com.pranay.easybuy.users.entity.Role;
import com.pranay.easybuy.users.entity.User;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

	@Mapping(target = "password", ignore = true)
	@Mapping(target = "role", source = "role")
	public abstract User toEntity(UserDto userDto);

	@AfterMapping
	protected void setDefaultRole(UserDto userDto, @MappingTarget User user) {
		if (user.getRole() == null) {
			user.setRole(Role.ROLE_USER);
		}
	}

//	@AfterMapping
//	protected void hashPassword(UserDto userDto, @MappingTarget User user) {
//		if (userDto != null && userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
//			String hashedPassword = passwordEncoder.encode(userDto.getPassword());
//			user.setPassword(hashedPassword);
//		}
//	}

	@Mapping(target = "password", ignore = true)
	public abstract UserDto toDto(User user);

	public abstract List<UserDto> toDtoList(List<User> users);

}
