package com.pranay.easybuy.cart_order.payload;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cart_id", nullable = false)
	private Cart cart;

	@Column(nullable = false)
	private UUID productId;

	@Column(nullable = false, length = 220)
	private String productTitle;

	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal unitPrice;

	@Column(nullable = false)
	private Integer discountPercent;

	@Column(nullable = false)
	private Integer quantity;

	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal lineTotal;
}
