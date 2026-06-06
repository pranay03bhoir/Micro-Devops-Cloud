package com.pranay.easybuy.products.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pranay.easybuy.products.dto.ProductDTO;
import com.pranay.easybuy.products.responseBuilder.PagedResponse;
import com.pranay.easybuy.products.services.ProductService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Validated
@RefreshScope
public class ProductController {

	@Value("${IMAGEKIT_FOLDER}")
	private String imageKitFolderPath;

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

	@GetMapping("/category/{categoryId}/products")
	public ResponseEntity<PagedResponse<ProductDTO>> getProductsByCategoryId(
			@PathVariable Long categoryId,
			@RequestParam(defaultValue = "0") @Min(value = 0, message = "Page value must br greater than or equal to 0") int page,
			@RequestParam(defaultValue = "12") @Min(value = 1, message = "Size must be greater than 0") @Max(value = 100, message = "Size must be at-least 100") int size) {
		return new ResponseEntity<>(productService.getProductsByCategory(categoryId, page, size), HttpStatus.OK);
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

	@GetMapping("/imagekit-folder-path")
	public ResponseEntity<String> getImageKitFolderPath() {
		return new ResponseEntity<>(imageKitFolderPath, HttpStatus.OK);
	}

}
