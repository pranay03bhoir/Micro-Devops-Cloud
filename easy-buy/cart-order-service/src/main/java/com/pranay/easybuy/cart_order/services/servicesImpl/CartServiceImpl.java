package com.pranay.easybuy.cart_order.services.servicesImpl;

import com.pranay.easybuy.cart_order.client.ProductClient;
import com.pranay.easybuy.cart_order.config.CartItemMapper;
import com.pranay.easybuy.cart_order.config.CartMapper;
import com.pranay.easybuy.cart_order.dto.AddCartItemRequest;
import com.pranay.easybuy.cart_order.dto.CartResponse;
import com.pranay.easybuy.cart_order.dto.ProductSnapshot;
import com.pranay.easybuy.cart_order.dto.UpdateCartItemRequest;
import com.pranay.easybuy.cart_order.exception.BusinessRuleException;
import com.pranay.easybuy.cart_order.exception.ExternalServiceException;
import com.pranay.easybuy.cart_order.exception.ResourceNotFoundException;
import com.pranay.easybuy.cart_order.payload.Cart;
import com.pranay.easybuy.cart_order.payload.CartItem;
import com.pranay.easybuy.cart_order.payload.CartStatus;
import com.pranay.easybuy.cart_order.repository.CartRepository;
import com.pranay.easybuy.cart_order.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductClient productClient;
    private final CartMapper cartMapper;
    private final CartItemMapper cartItemMapper;

    @Override
    @Transactional(readOnly = true)
    public CartResponse getCart(String userId) {
        Cart cart = getOrCreateUserCart(userId);
        return cartMapper.toCartResponse(cart);
    }

    @Override
    public CartResponse addItem(String userId, AddCartItemRequest request) {
        Cart cart = getOrCreateUserCart(userId);
        ProductSnapshot product = fetProduct(request.productId());
        CartItem item = cart.getItems().stream().filter(existing -> existing.getProductId().equals(request.productId())).findFirst().orElseGet(() -> {
            CartItem created = new CartItem();
            created.setCart(cart);
            created.setProductId(request.productId());
            created.setQuantity(request.quantity());
            cart.getItems().add(created);
            return created;
        });
        item.setProductTitle(product.title());
        item.setUnitPrice(finalUnitPrice(product.price(), product.discount()));
        item.setDiscountPercent(defaultZero(product.discount()));
        item.setQuantity(safeQuantity(item.getQuantity()) + request.quantity());
        return cartMapper.toCartResponse(cartRepository.save(cart));
    }

    @Override
    public CartResponse updateItem(String userId, String productId, UpdateCartItemRequest request) {
        Cart cart = getOrCreateUserCart(userId);
        CartItem cartItem = findCartItem(cart, parseProductId(productId));
        cartItem.setQuantity(request.quantity());
        return cartMapper.toCartResponse(cartRepository.save(cart));

    }

    @Override
    public CartResponse removeItem(String userId, String productId) {
        Cart cart = getOrCreateUserCart(userId);
        CartItem item = findCartItem(cart, parseProductId(productId));
        cart.getItems().remove(item);
        return cartMapper.toCartResponse(cartRepository.save(cart));
    }

    @Override
    public void clearCart(String userId) {
        Cart cart = getOrCreateUserCart(userId);
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    private Cart getOrCreateUserCart(String userId) {
        if (!StringUtils.hasText(userId)) {
            throw new BusinessRuleException("userId is required");
        }
        return cartRepository.findByUserIdAndStatus(normalizeUserId(userId), CartStatus.ACTIVE).orElseGet(() -> {
            Cart cart = new Cart();
            cart.setUserId(normalizeUserId(userId));
            cart.setStatus(CartStatus.ACTIVE);
            cart.setItems(new ArrayList<>());
            return cartRepository.save(cart);
        });
    }

    private ProductSnapshot fetProduct(UUID productId) {
        try {
            ProductSnapshot product = productClient.getProductById(productId);
            if (product == null || Boolean.FALSE.equals(product.live())) {
                throw new BusinessRuleException("Product is not available: " + productId);
            }
            return product;
        } catch (BusinessRuleException e) {
            throw e;
        } catch (Exception e) {
            throw new ExternalServiceException("Failed to load product " + productId + e);
        }
    }

    private CartItem findCartItem(Cart cart, UUID productId) {
        return cart
                .getItems()
                .stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(
                        () -> new ResourceNotFoundException("Cart item not found for productId: " + productId));
    }

    private UUID parseProductId(String productId) {
        try {
            return UUID.fromString(productId);
        } catch (IllegalArgumentException ex) {
            throw new BusinessRuleException("Invalid productId: " + productId);
        }
    }

    private BigDecimal finalUnitPrice(Double price, Integer discount) {
        BigDecimal base = BigDecimal.valueOf(price == null ? 0.0 : price);
        BigDecimal discountFactor = BigDecimal.valueOf(100 - defaultZero(discount)).divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
        return base.multiply(discountFactor).setScale(2, RoundingMode.HALF_UP);
    }

    private int safeQuantity(Integer quantity) {
        return quantity == null ? 0 : quantity;
    }

    private int defaultZero(Integer value) {
        return value == null ? 0 : value;
    }

    private String normalizeUserId(String userId) {
        return userId.trim();
    }

}
