package com.pranay.easybuy.cart_order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pranay.easybuy.cart_order.payload.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
