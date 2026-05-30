package com.pranay.easybuy.apigateway.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping
public class FallbackController {

    @GetMapping("/product-fallback")
    public Mono<String> productCircuitBreakerFallback() {
        return Mono.just("Our product service is down, contact our support team.");
    }
}
