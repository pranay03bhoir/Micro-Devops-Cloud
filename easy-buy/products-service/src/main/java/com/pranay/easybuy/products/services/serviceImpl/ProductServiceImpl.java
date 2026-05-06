package com.pranay.easybuy.products.services.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.pranay.easybuy.products.config.ReviewMapper;
import com.pranay.easybuy.products.dto.ReviewDTO;
import com.pranay.easybuy.products.exceptions.InvalidRequestException;
import com.pranay.easybuy.products.models.Review;
import com.pranay.easybuy.products.repositories.ReviewRepository;
import com.pranay.easybuy.products.services.ImageStorageService;
import org.springframework.stereotype.Service;

import com.pranay.easybuy.products.config.CategoryMapper;
import com.pranay.easybuy.products.config.ProductMapper;
import com.pranay.easybuy.products.dto.ProductDTO;
import com.pranay.easybuy.products.exceptions.ResourceNotFoundException;
import com.pranay.easybuy.products.models.Category;
import com.pranay.easybuy.products.models.Product;
import com.pranay.easybuy.products.repositories.CategoryRepository;
import com.pranay.easybuy.products.repositories.ProductRepository;
import com.pranay.easybuy.products.services.ProductService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;
	private final ProductMapper productMapper;
	private final CategoryRepository categoryRepository;
	private final CategoryMapper categoryMapper;
	private final ReviewMapper reviewMapper;
	private final ReviewRepository reviewRepository;
	private final ImageStorageService imageStorageService;

	@Override
	public ProductDTO createProduct(ProductDTO productDTO) {
		Product product = productMapper.toEntity(productDTO);
		Product savedProduct = productRepository.save(product);
		return productMapper.toDto(savedProduct);
	}

	@Override
	public List<ProductDTO> getAllProducts() {
		return productMapper.toDtoList(productRepository.findAll());
	}

	@Override
	public ProductDTO getProductById(UUID productId) {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found!!!"));
		return productMapper.toDto(product);
	}

	@Override
	public ProductDTO updateProduct(ProductDTO productDTO, UUID productId) {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found!!!"));
		product.setTitle(productDTO.getTitle());
		product.setShort_desc(productDTO.getShortDesc());
		product.setLong_desc(productDTO.getLongDesc());
		product.setPrice(productDTO.getPrice());
		product.setDiscount(productDTO.getDiscount());
		product.setProductImages(productDTO.getProductImages());
		product.setLive(productDTO.getLive());
		return productMapper.toDto(product);
	}

	@Override
	public String deleteProduct(UUID productId) {
		productRepository.deleteById(productId);
		return "Product deleted successfully";
	}

	@Override
	public List<ProductDTO> getProductsByCategory(Long categoryId) {
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found"));
		List<Product> products = productRepository.findProductsByCategoryId(categoryId).orElseThrow(
				() -> new ResourceNotFoundException("Products not found of category: " + category.getTitle()));
		return productMapper.toDtoList(products);
	}

	@Override
	public ProductDTO addCategoryToProduct(UUID productId, Long categoryId) {
		Category category = findCategory(categoryId);
		Product product = findProduct(productId);
		if (!product.getCategories().contains(category)) {
			product.getCategories().add(category);
		}
		if (!category.getProducts().contains(product)) {
			category.getProducts().add(product);
		}
		categoryRepository.save(category);
		productRepository.save(product);
		return productMapper.toDto(product);
	}

	@Override
	public ProductDTO removeCategoryFromProduct(UUID productId, Long categoryId) {
		Category category = findCategory(categoryId);
		Product product = findProduct(productId);
		product.getCategories().remove(category);
		category.getProducts().remove(product);
		categoryRepository.save(category);
		productRepository.save(product);
		return productMapper.toDto(product);
	}

	@Override
	public ReviewDTO addReviewToProduct(UUID productId, ReviewDTO reviewDTO) {
		Product product = findProduct(productId);
		Review review = reviewMapper.toEntity(reviewDTO);
		review.setProduct(product);
		product.getReviews().add(review);
		reviewRepository.save(review);
		productRepository.save(product);
		return reviewMapper.toDto(review);
	}

	@Override
	public ProductDTO uploadProductImages(UUID productId, List<MultipartFile> files) {
		Product product = findProduct(productId);
		List<String> uploadUrls = uploadImages(files);
		if (product.getProductImages() == null) {
			product.setProductImages(new ArrayList<>());
		}
		product.getProductImages().addAll(uploadUrls);
		return productMapper.toDto(product);
	}

	private Product findProduct(UUID productId) {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found!!!"));
		return product;
	}

	private Category findCategory(Long categoryId) {
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found!!!"));
		return category;
	}

	private List<String> uploadImages(List<MultipartFile> files) {
		if (files == null || files.isEmpty()) {
			throw new InvalidRequestException("At least one product image is required");
		}
		List<String> uploadedUrls = new ArrayList<>();
		for (MultipartFile file : files) {
			uploadedUrls.add(imageStorageService.uploadImage(file));
		}
		return uploadedUrls;
	}
}
