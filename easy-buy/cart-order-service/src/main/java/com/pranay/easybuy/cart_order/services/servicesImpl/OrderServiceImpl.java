package com.pranay.easybuy.cart_order.services.servicesImpl;

import com.pranay.easy_buy.events.OrderEvent;
import com.pranay.easybuy.cart_order.OrderCreateRequest;
import com.pranay.easybuy.cart_order.client.InventoryClient;
import com.pranay.easybuy.cart_order.client.ProductClient;
import com.pranay.easybuy.cart_order.config.OrderMapper;
import com.pranay.easybuy.cart_order.dto.*;
import com.pranay.easybuy.cart_order.exception.BusinessRuleException;
import com.pranay.easybuy.cart_order.exception.ExternalServiceException;
import com.pranay.easybuy.cart_order.exception.ResourceNotFoundException;
import com.pranay.easybuy.cart_order.payload.*;
import com.pranay.easybuy.cart_order.producer.OrderEventPublisher;
import com.pranay.easybuy.cart_order.repository.CartRepository;
import com.pranay.easybuy.cart_order.repository.OrderItemRepository;
import com.pranay.easybuy.cart_order.repository.OrderRepository;
import com.pranay.easybuy.cart_order.services.OrderService;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final RestTemplate restTemplate;
    private final RestClient restClient;
    private final ProductClient productClient;

    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final InventoryClient inventoryClient;
    private final OrderEventPublisher orderEventPublisher;
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
    @Retry(name = "createOrderRetry", fallbackMethod = "createOrderFallback")
    public ProductSnapshot createOrder(OrderCreateRequest orderCreateRequest) {
        log.info("retrying....");
        UUID productId = orderCreateRequest.orderItems().getFirst().productId();
        return this.getProduct(productId);
    }

    // Fallback for retry.
    public Object createOrderFallback(OrderCreateRequest orderCreateRequest, Throwable t) {
        log.info("Create order fallback");
        log.info("Exception {}", t.getMessage());
        return null;
    }

    @Override
    public OrderResponse checkout(String userId, CheckoutRequest request) {
        Cart cart = cartRepository.findByUserIdAndStatus(normalizeUserId(userId), CartStatus.ACTIVE).orElseThrow(() -> new ResourceNotFoundException("Active cart not found for userId: " + userId));
        if (cart.getItems().isEmpty()) {
            throw new BusinessRuleException("Cart is empty");
        }
        List<InventorySnapshot> reservedSnapshots = new ArrayList<>();
        try {
            for (CartItem item : cart.getItems()) {
                reservedSnapshots.add(inventoryClient.reserveByProductId(item.getProductId(), new ReserveStockRequest(item.getQuantity())));
            }

            Order order = buildOrderFromCart(cart, request);
            Order saved = orderRepository.save(order);
            saved = orderRepository.save(saved);

            cart.setStatus(CartStatus.CHECKED_OUT);
            cart.setCheckedOutAt(Instant.now());
            cart.getItems().clear();
            cartRepository.save(cart);

            //order event publish
            OrderEvent orderEvent = new OrderEvent();
            orderEvent.setOrderId(saved.getId());
            orderEvent.setMessage("Order is created successfully...");
            orderEvent.setTotalAmount(saved.getTotalAmount());
            orderEvent.setUserId(saved.getUserId());
            orderEvent.setStatus(saved.getStatus().toString());
            orderEventPublisher.publishOrderCreatedEvent(orderEvent);

            return orderMapper.toOrderResponse(saved);
        } catch (Exception e) {
            for (int i = reservedSnapshots.size() - 1; i >= 0; i--) {
                CartItem item = cart.getItems().get(i);
                try {
                    inventoryClient.releaseByProductId(item.getProductId(), new ReleaseStockRequest(item.getQuantity()));
                } catch (Exception releaseEx) {
                    throw new ExternalServiceException("Checkout failed and stock rollback also failed for productId: " + item.getProductId(), releaseEx);
                }
            }
            if (e instanceof ExternalServiceException externalServiceException) {
                throw externalServiceException;
            }
            throw new ExternalServiceException("Checkout failed", e);
        }

    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long orderId) {
        return orderMapper.toOrderResponse(orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found for id: " + orderId)));
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderByNumber(String orderNumber) {
        return orderMapper.toOrderResponse(orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found for orderNumber: " + orderNumber)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByUserId(String userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(normalizeUserId(userId))
                .stream()
                .map(orderMapper::toOrderResponse)
                .toList();
    }

    @Override
    public OrderResponse cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found for id: " + orderId));

        if (order.getStatus() == OrderStatus.CANCELLED) {
            return orderMapper.toOrderResponse(order);
        }
        for (OrderItem item : order.getItems()) {
            try {
                inventoryClient.releaseByProductId(item.getProductId(), new ReleaseStockRequest(item.getQuantity()));
            } catch (Exception ex) {
                throw new ExternalServiceException("Failed to release stock for productId: " + item.getProductId(), ex);
            }
        }
        order.setStatus(OrderStatus.CANCELLED);
        order.setCancelledAt(Instant.now());
        return orderMapper.toOrderResponse(orderRepository.save(order));
    }

    @Override
    public void releaseReservedStock(UUID productId, Integer quantity) {
        try {
            inventoryClient.releaseByProductId(productId, new ReleaseStockRequest(quantity));
        } catch (Exception ex) {
            throw new ExternalServiceException("Failed to release stock for productId: " + productId, ex);
        }
    }

    @Override
    public void updatePaymentStatus(Long orderId, String paymentStatus) {
        log.info("Updating payment status for Order ID: {} to {}", orderId, paymentStatus);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found for id: " + orderId));
        order.setPaymentStatus(PaymentStatus.valueOf(paymentStatus));
        orderRepository.save(order);
        log.info("Payment status successfully updated for Order ID: {}", orderId);
    }

    private String normalizeUserId(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new BusinessRuleException("userId is required");
        }
        return userId.trim();
    }

    private Order buildOrderFromCart(Cart cart, CheckoutRequest request) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setUserId(cart.getUserId());
        order.setBillingName(request.billingName().trim());
        order.setBillingPhone(request.billingPhone().trim());
        order.setExtraInformation(request.extraInformation().trim());
        order.setShippingAddress(request.shippingAddress().trim());
        order.setPaymentMethod(request.paymentMethod());
        order.setPaymentStatus(PaymentStatus.PENDING);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setItems(new ArrayList<>());

        BigDecimal total = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getItems()) {

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProductId(cartItem.getProductId());
            orderItem.setProductTitle(cartItem.getProductTitle());
            orderItem.setUnitPrice(cartItem.getUnitPrice());
            orderItem.setDiscountPercent(cartItem.getDiscountPercent());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setLineTotal(cartItem.getUnitPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())).setScale(2, RoundingMode.HALF_UP));
            order.getItems().add(orderItem);
            total = total.add(orderItem.getLineTotal());
        }
        order.setTotalAmount(total.setScale(2, RoundingMode.HALF_UP));
        return order;
    }

}
