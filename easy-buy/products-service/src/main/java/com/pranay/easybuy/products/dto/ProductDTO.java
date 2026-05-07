package com.pranay.easybuy.products.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
	private UUID id;

	@NotBlank(message = "Title is required.")
	private String title;

	@NotBlank(message = "Short description is requires.")
	@Size(max = 500, message = "Short description must be less than o equal to 500 characters.")
	private String short_desc;

	@NotBlank(message = "Long description is required.")
	private String long_desc;

	@NotNull(message = "Price is required.")
	@Positive(message = "Price must be greater than 0.")
	private Double price;

	@Min(value = 0, message = "discount must be greater than or equal to 0.")
	@Max(value = 100, message = "discount must be less than or equal to 100")
	private Integer discount;

	private Boolean live;
	private List<String> productImages;
	private List<CategoryDTO> categories;
	private List<ReviewDTO> reviews;
}
