package com.pranay.easybuy.users.dto;

import com.pranay.easybuy.users.entity.Role;

import java.util.UUID;


public record ChangeRoleRequest(UUID userId,
                                Role role) {

}
