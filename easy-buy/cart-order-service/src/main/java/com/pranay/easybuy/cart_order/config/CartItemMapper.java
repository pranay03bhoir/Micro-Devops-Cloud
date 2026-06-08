package com.pranay.easybuy.cart_order.config;

import com.pranay.easybuy.cart_order.dto.CartItemResponse;
import com.pranay.easybuy.cart_order.payload.CartItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
	CartItem toCartItemEntity(CartItemResponse cartItemResponse);

	CartItemResponse toCartItemResponse(CartItem cartItem);
}
