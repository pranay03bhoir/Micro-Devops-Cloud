package com.pranay.easybuy.cart_order.payload;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true, length = 36)
	private String orderNumber;

	@Column(nullable = false, length = 120)
	private String userId;

	@Column(nullable = false, length = 400)
	private String shippingAddress;

	@Column(length = 80)
	private String paymentMethod;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private OrderStatus status;

	@Column(nullable = false, precision = 14, scale = 2)
	private BigDecimal totalAmount;

	@Column(nullable = false, updatable = false)
	@CreatedDate
	private Instant createdAt;

	@Column(nullable = false)
	@LastModifiedDate
	private Instant updatedAt;

	@Column
	private Instant cancelledAt;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<OrderItem> items = new ArrayList<>();

	void onCreate() {
		if (status == null) {
			status = OrderStatus.CONFIRMED;
		}
		if (totalAmount == null) {
			totalAmount = BigDecimal.ZERO;
		}
	}
}
