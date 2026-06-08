package com.pranay.easybuy.cart_order.client;

import com.pranay.easybuy.cart_order.client.fallbacks.ProductClientFallback;
import com.pranay.easybuy.cart_order.dto.ProductResponse;

import com.pranay.easybuy.cart_order.dto.ProductSnapshot;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

//@FeignClient(name = "product-service", url = "http://localhost:8081/api") // Since we are using discovery service, we do not need to hardcode the service URL.
//@FeignClient(name = "PRODUCTS-SERVICE")
@FeignClient(name = "${product-service.name}", fallback = ProductClientFallback.class)
public interface ProductClient {

	@GetMapping("/api/products/{productId}")
	ProductSnapshot getProductById(@PathVariable UUID productId);

	// @PostMapping("/products/")
	// ProductResponse createProduct(@RequestBody ProductDTO productDTO);
}
