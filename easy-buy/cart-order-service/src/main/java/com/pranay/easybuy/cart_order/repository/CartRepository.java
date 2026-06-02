package com.pranay.easybuy.cart_order.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pranay.easybuy.cart_order.payload.Cart;
import com.pranay.easybuy.cart_order.payload.CartStatus;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserIdAndStatus(String userId, CartStatus status);
}
