package com.pranay.easybuy.apigateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

	private final String productServiceId;
	private final String cartOrderServiceId;

	public RouteConfig(@Value("${product.service.id}") String productServiceId,
			@Value("${cart-order.service.id}") String cartOrderServiceId) {
		this.productServiceId = productServiceId;
		this.cartOrderServiceId = cartOrderServiceId;
	}

	@Bean
	public RouteLocator route(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("products-service",
						route -> route.path("/products-service/**")
								.filters(f -> f.rewritePath("/products-service/?(?<remaining>.*)", "/${remaining}"))
								.uri(productServiceId))
				.route("cart-order-service",
						route -> route.path("/cart-order-service/**")
								.filters(f -> f.rewritePath("/cart-order-service?(?<remaining>.*)", "/${remaining}"))
								.uri(cartOrderServiceId))

				.build();
	}
}
