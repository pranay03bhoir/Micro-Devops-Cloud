package com.pranay.easybuy.cart_order.config;

import com.pranay.easybuy.cart_order.dto.CartResponse;
import com.pranay.easybuy.cart_order.payload.Cart;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {

	Cart toCartEntity(CartResponse cartResponse);

	CartResponse toCartResponse(Cart cart);
}
