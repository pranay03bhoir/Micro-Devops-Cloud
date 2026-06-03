package com.pranay.easybuy.users.dto;

import java.util.UUID;

import com.pranay.easybuy.users.entity.Role;


public record ChangeRoleRequest(UUID userId,
        Role role) {

}
