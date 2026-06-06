package com.pranay.easybuy.users.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pranay.easybuy.users.entity.User;

public interface UserRepository extends JpaRepository<User, UUID> {
    // find the user by email
    Optional<User> findByEmail(String email);

    // check the user exist by email or not
    Boolean existsByEmail(String email);
}
