package com.pranay.easybuy.cart_order;

import java.util.UUID;

public record OrderItems(UUID productId, Integer quantity) {
}
