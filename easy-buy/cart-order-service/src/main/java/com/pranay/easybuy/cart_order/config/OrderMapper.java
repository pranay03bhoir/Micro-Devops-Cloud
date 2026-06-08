package com.pranay.easybuy.cart_order.config;

import com.pranay.easybuy.cart_order.dto.OrderResponse;
import com.pranay.easybuy.cart_order.payload.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    Order toOrderEntity(OrderResponse orderResponse);

    OrderResponse toOrderResponse(Order order);
}
