package com.pranay.easybuy.cart_order;

import java.util.List;

public record OrderCreateRequest(List<OrderItems> orderItems) {
}
