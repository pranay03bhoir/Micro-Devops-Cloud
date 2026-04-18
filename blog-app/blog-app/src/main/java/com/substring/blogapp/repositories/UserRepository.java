package com.substring.blogapp.repositories;

import com.substring.blogapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
