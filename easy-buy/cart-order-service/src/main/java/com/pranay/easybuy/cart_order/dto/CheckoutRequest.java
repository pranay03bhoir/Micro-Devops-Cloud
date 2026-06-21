package com.pranay.easybuy.cart_order.dto;

import com.pranay.easybuy.cart_order.payload.PaymentMethod;
import jakarta.validation.constraints.NotBlank;

public record CheckoutRequest(@NotBlank String billingName,
                              @NotBlank String billingPhone,
                              @NotBlank String shippingAddress,
                              PaymentMethod paymentMethod,
                              String extraInformation,
                              String paymentDetails) {

}
