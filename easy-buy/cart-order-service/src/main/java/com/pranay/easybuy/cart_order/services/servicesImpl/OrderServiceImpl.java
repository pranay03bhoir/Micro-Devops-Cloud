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
	public String createOrder(OrderCreateRequest orderCreateRequest) {

		String productId = orderCreateRequest.orderItems().getFirst().productId();
		ProductResponse product = this.getProduct(productId);
		return "Order created";
	}

	private ProductResponse getProduct(String productId) {
		try {
			var productUrl = "http://localhost:8081/api/products/" + productId;
			log.info("get product url {}", productUrl);

			return productClientTest.getProductById(productId);

//			=======================================================================================
			// Using RestClient to call the product service.
//			ProductResponse productResponse = restClient.get().uri(productUrl)
//					.header(HttpHeaders.ACCEPT, "application/json").header(HttpHeaders.AUTHORIZATION, "").retrieve()
//					.body(ProductResponse.class);
//			return productResponse;
//			===============================================================================================
			// Below code used RestTemplate
			// call product service using restTemplate

//			ResponseEntity<ProductResponse> productResponse = restTemplate.getForEntity(productUrl,
//					ProductResponse.class);
			// validation logic
//			if (productResponse.getStatusCode().is2xxSuccessful()) {
//				log.info("we got successful response from product service {}", productResponse);
//			}

//			ProductResponse productResponse = restTemplate.getForObject(productUrl, ProductResponse.class);

//			log.info("get product response {}", productResponse);
//			return productResponse.getBody();
		} catch (HttpClientErrorException e) {
			e.printStackTrace();
			throw new RuntimeException("Product not found" + e.getStatusCode());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Something went wrong");
		}

	}
}
