package com.pranay.easybuy.cart_order;

import com.pranay.easybuy.cart_order.services.OrderService;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test/orders")
@RequiredArgsConstructor
public class OrderTestController {

	private Logger logger = LoggerFactory.getLogger(OrderTestController.class);
	private final OrderService orderService;

	@PostMapping
	@RateLimiter(name = "createOrderRateLimiter", fallbackMethod = "createOrderRateLimiterFallback")
	public ResponseEntity<Object> createOrder(@RequestBody OrderCreateRequest request) {
		// logger.info("Request object: {}", request);
		// return ResponseEntity.ok("Order created");
		logger.info("Retrying.....");
		logger.info("Order created request received {}", request);
		// if (2 < 5) {
		// throw new NullPointerException("Request failed");
		// }
		return ResponseEntity.ok(orderService.createOrder(request));
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<String> handleException(RuntimeException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
