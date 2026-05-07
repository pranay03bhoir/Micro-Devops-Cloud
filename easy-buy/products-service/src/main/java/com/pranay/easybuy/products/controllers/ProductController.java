package com.pranay.easybuy.products.controllers;

import com.pranay.easybuy.products.dto.ProductDTO;
import com.pranay.easybuy.products.responseBuilder.PagedResponse;
import com.pranay.easybuy.products.services.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Validated
public class ProductController {

	private final ProductService productService;

	@PostMapping("/")
	public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO) {
		return new ResponseEntity<>(productService.createProduct(productDTO), HttpStatus.CREATED);
	}

	@GetMapping("/")
	public ResponseEntity<PagedResponse<ProductDTO>> getAllProducts(
			@RequestParam(defaultValue = "0") @Min(value = 0, message = "Page must be greater than or equal to 0") int page,
			@RequestParam(defaultValue = "12") @Min(value = 1, message = "Size must be greater than 0") @Max(value = 100, message = "Size must be at most 100") int size) {
		return new ResponseEntity<>(productService.getAllProducts(page, size), HttpStatus.OK);
	}

}
