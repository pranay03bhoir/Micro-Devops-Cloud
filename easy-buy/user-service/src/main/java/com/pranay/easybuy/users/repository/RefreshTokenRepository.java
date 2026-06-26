package com.pranay.easybuy.users.repository;

import com.pranay.easybuy.users.entity.RefreshToken;
import com.pranay.easybuy.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    Optional<RefreshToken> findByRefreshTokenAndUserId(String refreshToken, UUID user_id);

    Optional<RefreshToken> findByUser(User user);
}
