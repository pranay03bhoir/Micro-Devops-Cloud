package com.pranay.easybuy.apigateway;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import reactor.core.publisher.Mono;

@Configuration
public class RouteConfig {

    private final String productServiceId;
    private final String cartOrderServiceId;

    public RouteConfig(
            @Value("${product.service.id}") String productServiceId,
            @Value("${cartOrder.service.id}") String cartOrderServiceId) {
        this.productServiceId = productServiceId;
        this.cartOrderServiceId = cartOrderServiceId;
    }

    @Bean
    public RouteLocator route(RouteLocatorBuilder builder) {
        return builder
                .routes()
                .route(
                        "products-service",
                        route -> route
                                .path("/products-service/**")
                                .filters(
                                        f -> f.requestRateLimiter(
                                                rateLimitConfig -> rateLimitConfig
                                                        .setKeyResolver(keyResolver())
                                                        .setRateLimiter(redisRateLimiter()))
                                                .circuitBreaker(
                                                        c -> c.setName("productCircuitBreaker")
                                                                .setFallbackUri("forward:/product-fallback"))
                                                .rewritePath(
                                                        "/products-service/?(?<remaining>.*)", "/${remaining}"))
                                .uri(productServiceId))
                .route(
                        "cart-order-service",
                        route -> route
                                .path("/cart-order-service/**")
                                .filters(
                                        f -> f.rewritePath("/cart-order-service?(?<remaining>.*)", "/${remaining}")
                                                .retry(
                                                        retryConfig -> retryConfig
                                                                .setRetries(3)
                                                                .setBackoff(
                                                                        Duration.ofMillis(100),
                                                                        Duration.ofMillis(1000),
                                                                        2,
                                                                        true)
                                                                .setMethods(HttpMethod.GET, HttpMethod.POST)))
                                .uri(cartOrderServiceId))
                .build();
    }

    @Bean
    public KeyResolver keyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getHeaders().getFirst("user"));
    }

    @Bean
    public RedisRateLimiter redisRateLimiter() {
        return new RedisRateLimiter(1, 1, 1);
    }
    // If we want different routes for different profiles we can do that with the
    // @Profile annotations
    // Example -
    // @Bean
    // @Profile("dev") // or @Profile("prod")
    // public RouteLocator route(RouteLocatorBuilder builder) {
    // return builder.routes()
    // .route("products-service",
    // route -> route.path("/products-service/**")
    // .filters(f -> f.rewritePath("/products-service/?(?<remaining>.*)",
    // "/${remaining}"))
    // .uri(productServiceId))
    // .route("cart-order-service",
    // route -> route.path("/cart-order-service/**")
    // .filters(f -> f.rewritePath("/cart-order-service?(?<remaining>.*)",
    // "/${remaining}"))
    // .uri(cartOrderServiceId))
    //
    // .build();
    // }

}
