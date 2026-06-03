package com.pranay.easybuy.cart_order.services;

import org.springframework.stereotype.Service;

import com.pranay.easybuy.cart_order.dto.AddCartItemRequest;
import com.pranay.easybuy.cart_order.dto.CartResponse;
import com.pranay.easybuy.cart_order.dto.UpdateCartItemRequest;

@Service
public interface CartService {
    CartResponse getCart(String userId);

    CartResponse addItem(String userId, AddCartItemRequest request);

    CartResponse updateItem(String userId, String productId, UpdateCartItemRequest request);

    CartResponse removeItem(String userId, String productId);

    void clearCart(String userId);
}
