package com.pranay.easybuy.cart_order.services.servicesImpl;

import com.pranay.easybuy.cart_order.OrderCreateRequest;
import com.pranay.easybuy.cart_order.client.ProductClientTest;
import com.pranay.easybuy.cart_order.payload.ProductResponse;
import com.pranay.easybuy.cart_order.services.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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
	private final ProductClientTest productClientTest;

	@Override
	public Object createOrder(OrderCreateRequest orderCreateRequest) {
		if (orderCreateRequest.orderItems() == null || orderCreateRequest.orderItems().isEmpty()) {
			throw new IllegalArgumentException("Order items cannot be null or empty");
		}
		String productId = orderCreateRequest.orderItems().getFirst().productId();
		ProductResponse product = this.getProduct(productId);

		// Example: Replace this with real order creation logic. For now, return dummy
		// order ID.
		return product;
	}

	private ProductResponse getProduct(String productId) {
		try {
			ProductResponse productbyId = productClientTest.getProductById(productId);
			if (productbyId == null) {
				ProductResponse productResponse = new ProductResponse(
						productId,
						"Demo product",
						"This is a demo product",
						"this is long desc demo product",
						23423.88,
						500,
						true,
						java.util.Collections.emptyList());

				return productResponse;
			}
			return productClientTest.getProductById(productId);

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
}
