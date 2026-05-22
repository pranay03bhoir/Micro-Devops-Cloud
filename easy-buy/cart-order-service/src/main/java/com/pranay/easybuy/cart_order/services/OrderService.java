package com.pranay.easybuy.cart_order.services;

import com.pranay.easybuy.cart_order.OrderCreateRequest;
import org.springframework.stereotype.Service;

@Service
public interface OrderService {
	String createOrder(OrderCreateRequest orderCreateRequest);
}
