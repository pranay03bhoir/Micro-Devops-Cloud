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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Validated
public class ProductController {

	private final ProductService productService;

	@GetMapping("/")
	public ResponseEntity<PagedResponse<ProductDTO>> getAllProducts(
			@RequestParam(defaultValue = "0") @Min(value = 0, message = "Page must be greater than or equal to 0") int page,
			@RequestParam(defaultValue = "12") @Min(value = 1, message = "Size must be greater than 0") @Max(value = 100, message = "Size must be at most 100") int size) {
		return new ResponseEntity<>(productService.getAllProducts(page, size), HttpStatus.OK);
	}

	@GetMapping("/{productId}")
	public ResponseEntity<ProductDTO> getProductById(@PathVariable UUID productId) {
		return new ResponseEntity<>(productService.getProductById(productId), HttpStatus.OK);
	}

	public ResponseEntity<List<ProductDTO>> getProductsByCategoryId(@PathVariable Long categoryId) {
		return new ResponseEntity<>(productService.getProductsByCategory(categoryId), HttpStatus.OK);
	}

	@PostMapping("/")
	public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO) {
		return new ResponseEntity<>(productService.createProduct(productDTO), HttpStatus.CREATED);
	}

	@PutMapping("/{productId}")
	public ResponseEntity<ProductDTO> updateProduct(@PathVariable UUID productId, @RequestBody ProductDTO productDTO) {
		return new ResponseEntity<>(productService.updateProduct(productDTO, productId), HttpStatus.OK);
	}

	@DeleteMapping("/{productId}")
	public ResponseEntity<String> deleteProduct(@PathVariable UUID productId) {
		return new ResponseEntity<>(productService.deleteProduct(productId), HttpStatus.OK);
	}

	@PostMapping("/{productId}/categories/{categoryId}")
	public ResponseEntity<ProductDTO> addCategoryToProduct(@PathVariable UUID productId,
			@PathVariable Long categoryId) {
		return new ResponseEntity<>(productService.addCategoryToProduct(productId, categoryId), HttpStatus.OK);
	}

	@DeleteMapping("/{productId}/categories/{categoryId}")
	public ResponseEntity<ProductDTO> removeCategoryFromProduct(@PathVariable UUID productId,
			@PathVariable Long categoryId) {
		return new ResponseEntity<>(productService.removeCategoryFromProduct(productId, categoryId), HttpStatus.OK);
	}

	@PostMapping(value = "/{productId}/images", consumes = "multipart/form-data")
	public ResponseEntity<ProductDTO> addProductImages(@PathVariable UUID productId,
			@RequestParam("files") List<MultipartFile> files) {
		return new ResponseEntity<>(productService.addProductImages(productId, files), HttpStatus.OK);
	}

}
