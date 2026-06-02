package com.pranay.easybuy.products.services;

import java.util.List;
import java.util.UUID;

import com.pranay.easybuy.products.dto.ReviewDTO;
import com.pranay.easybuy.products.responseBuilder.PagedResponse;
import org.springframework.stereotype.Service;

import com.pranay.easybuy.products.dto.ProductDTO;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface ProductService {

	ProductDTO createProduct(ProductDTO productDTO);

	// List<ProductDTO> getAllProducts();

	PagedResponse<ProductDTO> getAllProducts(int page, int size);

	ProductDTO getProductById(UUID productId);

	ProductDTO updateProduct(ProductDTO productDTO, UUID productId);

	String deleteProduct(UUID productId);

	PagedResponse<ProductDTO> getProductsByCategory(Long categoryId, int page, int size);

	ProductDTO addCategoryToProduct(UUID productId, Long categoryId);

	ProductDTO removeCategoryFromProduct(UUID productId, Long categoryId);

	ReviewDTO addReviewToProduct(UUID productId, ReviewDTO reviewDTO);

	ProductDTO addProductImages(UUID productId, List<MultipartFile> files);
}
