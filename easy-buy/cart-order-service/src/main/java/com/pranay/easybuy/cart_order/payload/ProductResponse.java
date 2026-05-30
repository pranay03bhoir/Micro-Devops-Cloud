package com.pranay.easybuy.cart_order.payload;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class ProductResponse {
	private String id;

	private String title;

	private String short_desc;

	private String long_desc;

	private Double price;

	private Integer discount;

	private Boolean live;
	private List<String> productImages;

	public ProductResponse(String id, String title, String short_desc, String long_desc, Double price, Integer discount,
			Boolean live, List<String> productImages) {
		this.id = id;
		this.title = title;
		this.short_desc = short_desc;
		this.long_desc = long_desc;
		this.price = price;
		this.discount = discount;
		this.live = live;
		this.productImages = productImages;
	}

}
