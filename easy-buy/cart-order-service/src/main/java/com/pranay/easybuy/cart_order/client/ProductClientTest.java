package com.pranay.easybuy.cart_order.client;

import com.pranay.easybuy.cart_order.client.fallbacks.ProductClientFallback;
import com.pranay.easybuy.cart_order.payload.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

//@FeignClient(name = "product-service", url = "http://localhost:8081/api") // Since we are using discovery service, we do not need to hardcode the service URL.
//@FeignClient(name = "PRODUCTS-SERVICE")
@FeignClient(name = "${product-service.name}", fallback = ProductClientFallback.class)
public interface ProductClientTest {

	@GetMapping("/api/products/{productId}")
	ProductResponse getProductById(@PathVariable String productId);

	// @PostMapping("/products/")
	// ProductResponse createProduct(@RequestBody ProductDTO productDTO);
}
