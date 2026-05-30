package com.pranay.easybuy.cart_order;

import com.pranay.easybuy.cart_order.payload.ProductResponse;
import com.pranay.easybuy.cart_order.services.OrderService;
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
	public ResponseEntity<Object> createOrder(@RequestBody OrderCreateRequest request) {
		logger.info("Request object: {}", request);
		// return ResponseEntity.ok("Order created");
		return ResponseEntity.ok(orderService.createOrder(request));
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<String> handleException(RuntimeException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
