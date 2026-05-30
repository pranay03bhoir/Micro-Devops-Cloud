package com.pranay.easybuy.cart_order.client.fallbacks;

import org.springframework.stereotype.Component;

import com.pranay.easybuy.cart_order.client.ProductClientTest;
import com.pranay.easybuy.cart_order.payload.ProductResponse;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ProductClientFallback implements ProductClientTest {

    @Override
    public ProductResponse getProductById(String productId) {
        log.info("product fallback");
        return null;
    }

}
