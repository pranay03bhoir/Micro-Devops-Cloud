package com.pranay.easybuy.cart_order.payload;

import jakarta.validation.constraints.*;
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
}
