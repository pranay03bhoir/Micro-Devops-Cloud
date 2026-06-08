package com.pranay.easybuy.cart_order.services.servicesImpl;

import com.pranay.easybuy.cart_order.OrderCreateRequest;
import com.pranay.easybuy.cart_order.client.ProductClient;
import com.pranay.easybuy.cart_order.dto.CheckoutRequest;
import com.pranay.easybuy.cart_order.dto.OrderResponse;
import com.pranay.easybuy.cart_order.dto.ProductResponse;
import com.pranay.easybuy.cart_order.dto.ProductSnapshot;
import com.pranay.easybuy.cart_order.services.OrderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
	private final RestTemplate restTemplate;
	private final RestClient restClient;
	private final ProductClient productClient;

	// @Override
	// @Retry(name = "createOrderRetry", fallbackMethod = "createOrderFallback")
	// public Object createOrder(OrderCreateRequest orderCreateRequest) {
	// if (orderCreateRequest.orderItems() == null ||
	// orderCreateRequest.orderItems().isEmpty()) {
	// throw new IllegalArgumentException("Order items cannot be null or empty");
	// }
	// log.info("Retrying.......");
	// String productId = orderCreateRequest.orderItems().getFirst().productId();
	// // if (2 < 3) {
	// // throw new RuntimeException("Product not found");
	// // }
	// ProductResponse product = this.getProduct(productId);

	// // Example: Replace this with real order creation logic. For now, return
	// dummy
	// // order ID.
	// return product;
	// }

	// Fallback for retry.
	public Object createOrderFallback(OrderCreateRequest orderCreateRequest, Throwable t) {
		log.info("Create order fallback");
		log.info("Exception {}", t.getMessage());
		return null;
	}

	private ProductSnapshot getProduct(UUID productId) {
		try {
			ProductSnapshot productbyId = productClient.getProductById(productId);
			if (productbyId == null) {
				ProductSnapshot product = new ProductSnapshot(productId, "Demo product", "This is a demo product",
						"this is long desc demo product", 23423.88, 500, true);

				return product;
			}
			return productClient.getProductById(productId);

			// var productUrl = "http://localhost:8081/api/products/" + productId;
			// log.info("get product url {}", productUrl);
			// =======================================================================================
			// Using RestClient to call the product service.
			// ProductResponse productResponse = restClient.get().uri(productUrl)
			// .header(HttpHeaders.ACCEPT,
			// "application/json").header(HttpHeaders.AUTHORIZATION, "").retrieve()
			// .body(ProductResponse.class);
			// return productResponse;
			// ===============================================================================================
			// Below code used RestTemplate
			// call product service using restTemplate

			// ResponseEntity<ProductResponse> productResponse =
			// restTemplate.getForEntity(productUrl,
			// ProductResponse.class);
			// validation logic
			// if (productResponse.getStatusCode().is2xxSuccessful()) {
			// log.info("we got successful response from product service {}",
			// productResponse);
			// }

			// ProductResponse productResponse = restTemplate.getForObject(productUrl,
			// ProductResponse.class);

			// log.info("get product response {}", productResponse);
			// return productResponse.getBody();
		} catch (HttpClientErrorException e) {
			e.printStackTrace();
			throw new RuntimeException("Product not found" + e.getStatusCode());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Something went wrong");
		}

	}

	@Override
	public ProductSnapshot createOrder(OrderCreateRequest orderCreateRequest) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'createOrder'");
	}

	@Override
	public OrderResponse checkout(String userId, CheckoutRequest request) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'checkout'");
	}

	@Override
	public OrderResponse getOrderById(Long orderId) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getOrderById'");
	}

	@Override
	public OrderResponse getOrderByNumber(String orderNumber) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getOrderByNumber'");
	}

	@Override
	public List<OrderResponse> getOrdersByUserId(String userId) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getOrdersByUserId'");
	}

	@Override
	public OrderResponse cancelOrder(Long orderId) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'cancelOrder'");
	}

	@Override
	public void releaseReservedStock(UUID productId, Integer quantity) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'releaseReservedStock'");
	}

}
