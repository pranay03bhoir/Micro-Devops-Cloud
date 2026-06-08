package com.pranay.easybuy.cart_order.client.fallbacks;

import com.pranay.easybuy.cart_order.dto.ProductSnapshot;
import org.springframework.stereotype.Component;

import com.pranay.easybuy.cart_order.client.ProductClient;
import com.pranay.easybuy.cart_order.dto.ProductResponse;

import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Component
@Slf4j
public class ProductClientFallback implements ProductClient {

    @Override
    public ProductSnapshot getProductById(UUID productId) {
        log.info("product fallback");
        return null;
    }

}
