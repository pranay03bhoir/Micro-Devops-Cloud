package com.pranay.easybuy.cart_order.services;

import com.pranay.easybuy.cart_order.OrderCreateRequest;
import com.pranay.easybuy.cart_order.dto.CheckoutRequest;
import com.pranay.easybuy.cart_order.dto.OrderResponse;
import com.pranay.easybuy.cart_order.dto.ProductSnapshot;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface OrderService {
    // Object createOrder(OrderCreateRequest orderCreateRequest);

    ProductSnapshot createOrder(OrderCreateRequest orderCreateRequest);

    OrderResponse checkout(String userId, CheckoutRequest request);

    OrderResponse getOrderById(Long orderId);

    OrderResponse getOrderByNumber(String orderNumber);

    List<OrderResponse> getOrdersByUserId(String userId);

    OrderResponse cancelOrder(Long orderId);

    void releaseReservedStock(UUID productId, Integer quantity);

    void updatePaymentStatus(Long orderId, String paymentStatus);
}
